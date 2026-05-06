package com.iikun.anichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.ConversationMember;
import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.MessageActionLog;
import com.iikun.anichat.entity.dto.SendMessageDTO;
import com.iikun.anichat.mapper.*;
import com.iikun.anichat.service.MessageService;
import com.iikun.common.base.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iikun.anichat.service.MessageReadStatusService;
import com.iikun.anichat.ws.ChatPushService;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息服务实现类
 *
 * @author iikun
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final ConversationMapper conversationMapper;
    private final ConversationMemberMapper memberMapper;
    private final MessageActionLogMapper actionLogMapper;
    private final MessageReadStatusMapper readStatusMapper;
    private final MessageReadStatusService messageReadStatusService;
    private final ObjectMapper objectMapper;
    private final ChatPushService chatPushService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Message> sendMessage(String fromUserId, SendMessageDTO sendDTO) {
        // 1. 检查会话是否存在
        Conversation conversation = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getConversationId, sendDTO.getConversationId()));
        if (conversation == null) {
            return Result.failed("会话不存在");
        }

        // 2. 检查用户是否在会话中
        Long memberCount = memberMapper.selectCount(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getConversationId, sendDTO.getConversationId())
                .eq(ConversationMember::getUserId, fromUserId));
        if (memberCount == 0) {
            return Result.failed("你不在该会话中，无法发送消息");
        }

        // 3. 创建消息
        Message message = new Message();
        message.setMessageId(UUID.randomUUID().toString().replace("-", ""));
        message.setConversationId(sendDTO.getConversationId());
        message.setFromUser(fromUserId);
        message.setType(sendDTO.getType());
        message.setContent(sendDTO.getContent());
        
        // 处理 JSON 类型的附件字段：确保符合 MySQL JSON 格式要求
        message.setAttachment(processJsonField(sendDTO.getAttachment()));
        
        message.setRecalled(0);
        message.setDeleted(0);
        
        // 生成顺序号（简单实现，实际生产环境建议用分布式 ID 且会话内自增）
        // 这里的 seq 可以使用 Redis 自增或 Snowflake 趋势递增
        message.setSeq(System.currentTimeMillis());

        this.save(message);

        // 4. 更新会话的 updateTime，便于排序
        conversation.setUpdateTime(LocalDateTime.now());
        conversationMapper.updateById(conversation);

        // 5. WebSocket 实时推送：把这条消息推给会话内除发送者外的所有成员。
        //    事务尚未提交也直接推（提交失败概率极小）；如需更强一致性，后续可改为
        //    TransactionSynchronizationManager.registerSynchronization 在 afterCommit 推。
        try {
            List<String> recipients = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                    .eq(ConversationMember::getConversationId, sendDTO.getConversationId())
                    .ne(ConversationMember::getUserId, fromUserId))
                    .stream().map(ConversationMember::getUserId).collect(Collectors.toList());
            if (!recipients.isEmpty()) {
                Map<String, Object> envelope = new HashMap<>();
                envelope.put("type", "message");
                envelope.put("payload", message);
                for (String to : recipients) {
                    chatPushService.pushToUser(to, envelope);
                }
            }
        } catch (Exception e) {
            // 推送失败仅记录日志：消息已入库，对方下次拉历史能看到，不影响主流程
            log.warn("WS 推送失败：conversationId={}, msg={}", sendDTO.getConversationId(), e.getMessage());
        }

        return Result.success(message);
    }

    /**
     * 处理 JSON 类型的字段，确保其符合 MySQL JSON 格式要求
     *
     * @param jsonValue 原始 JSON 字符串
     * @return 处理后的 JSON 字符串或 null
     */
    private String processJsonField(String jsonValue) {
        if (jsonValue == null || jsonValue.trim().isEmpty()) {
            return null;
        }
        String trimmed = jsonValue.trim();
        // 如果已经是以 { } 或 [ ] 或 " " 包裹，尝试校验是否为合法 JSON
        if ((trimmed.startsWith("{") && trimmed.endsWith("}")) ||
            (trimmed.startsWith("[") && trimmed.endsWith("]")) ||
            (trimmed.startsWith("\"") && trimmed.endsWith("\""))) {
            try {
                // 尝试解析，如果不报错说明是合法 JSON
                objectMapper.readTree(trimmed);
                return trimmed;
            } catch (Exception ignored) {
                // 解析失败，说明虽然有包裹但格式不对，后续按普通字符串处理
            }
        }
        // 如果不是合法的 JSON 结构，则将其作为普通 JSON 字符串处理（自动加双引号并转义）
        try {
            return objectMapper.writeValueAsString(trimmed);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Result<List<Message>> getHistoryMessages(String conversationId, String lastMessageId, Integer pageSize) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<Message>()
                .eq(Message::getConversationId, conversationId)
                .eq(Message::getDeleted, 0) // 只查询未删除的
                .orderByDesc(Message::getCreateTime)
                .last("limit " + (pageSize == null ? 20 : pageSize));

        if (lastMessageId != null && !lastMessageId.isEmpty()) {
            Message lastMsg = this.getOne(new LambdaQueryWrapper<Message>()
                    .eq(Message::getMessageId, lastMessageId));
            if (lastMsg != null) {
                queryWrapper.lt(Message::getCreateTime, lastMsg.getCreateTime());
            }
        }

        List<Message> messages = this.list(queryWrapper);
        return Result.success(messages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> recallMessage(String operatorId, String messageId) {
        Message message = this.getOne(new LambdaQueryWrapper<Message>()
                .eq(Message::getMessageId, messageId));
        if (message == null) {
            return Result.failed("消息不存在");
        }

        // 检查权限：只有发送者或群管理员/群主可以撤回
        if (!message.getFromUser().equals(operatorId)) {
            // 检查是否为群管理
            Conversation conversation = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                    .eq(Conversation::getConversationId, message.getConversationId()));
            
            ConversationMember member = memberMapper.selectOne(new LambdaQueryWrapper<ConversationMember>()
                    .eq(ConversationMember::getConversationId, message.getConversationId())
                    .eq(ConversationMember::getUserId, operatorId));

            if (member == null || (member.getRole() < 2 && !conversation.getOwnerId().equals(operatorId))) {
                return Result.failed("无权撤回该消息");
            }
        }

        // 检查撤回时间（限制在 2 分钟内）
        if (message.getCreateTime().plusMinutes(2).isBefore(LocalDateTime.now())) {
             // 检查是否为管理人员（角色 >= 2 为管理/群主）
             ConversationMember member = memberMapper.selectOne(new LambdaQueryWrapper<ConversationMember>()
                    .eq(ConversationMember::getConversationId, message.getConversationId())
                    .eq(ConversationMember::getUserId, operatorId));
             if (member == null || member.getRole() < 2) {
                 return Result.failed("普通用户只能撤回 2 分钟内的消息");
             }
        }

        // 执行撤回
        message.setRecalled(1);
        this.updateById(message);

        // 记录日志
        MessageActionLog actionLog = new MessageActionLog();
        actionLog.setMessageId(messageId);
        actionLog.setOperatorId(operatorId);
        actionLog.setAction("recall");
        actionLogMapper.insert(actionLog);

        return Result.success(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteMessage(String userId, String messageId) {
        Message message = this.getOne(new LambdaQueryWrapper<Message>().eq(Message::getMessageId, messageId));
        if (message == null) {
            return Result.failed("消息不存在");
        }
        // 权限校验：只能删除自己发的消息，或者在该会话中具备管理权限
        if (!message.getFromUser().equals(userId)) {
            ConversationMember member = memberMapper.selectOne(new LambdaQueryWrapper<ConversationMember>()
                    .eq(ConversationMember::getConversationId, message.getConversationId())
                    .eq(ConversationMember::getUserId, userId));
            if (member == null || member.getRole() < 2) {
                return Result.failed("无权删除该消息");
            }
        }
        // 执行逻辑删除
        message.setDeleted(1);
        this.updateById(message);
        return Result.success(null);
    }

    @Override
    public Result<Void> markRead(String userId, String conversationId) {
        return messageReadStatusService.markConversationAsRead(userId, conversationId);
    }
}
