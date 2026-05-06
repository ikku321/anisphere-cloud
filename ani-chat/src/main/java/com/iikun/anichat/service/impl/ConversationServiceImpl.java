package com.iikun.anichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.ConversationMember;
import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.MessageReadStatus;
import com.iikun.anichat.entity.dto.ConversationListItemDTO;
import com.iikun.anichat.mapper.ConversationMapper;
import com.iikun.anichat.mapper.ConversationMemberMapper;
import com.iikun.anichat.mapper.MessageMapper;
import com.iikun.anichat.mapper.MessageReadStatusMapper;
import com.iikun.anichat.service.ConversationService;
import com.iikun.common.base.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 会话服务实现类
 *
 * @author iikun
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    private final ConversationMemberMapper memberMapper;
    private final MessageMapper messageMapper;
    private final MessageReadStatusMapper readStatusMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Conversation> createOrGetPrivateConversation(String userId1, String userId2) {
        // 查找两个用户之间是否已经存在私聊会话
        // 简单逻辑：找到包含这两个人的所有私聊会话，检查是否只有这两个人
        // 为了性能，通常会有一个专门的索引或关系表。这里我们先查找 userId1 的所有私聊会话，再过滤。
        
        List<String> convIds1 = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId1))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        List<String> convIds2 = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId2))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        // 交集
        convIds1.retainAll(convIds2);

        for (String convId : convIds1) {
            Conversation conv = this.getOne(new LambdaQueryWrapper<Conversation>()
                    .eq(Conversation::getConversationId, convId)
                    .eq(Conversation::getType, 1)); // 1=私聊
            if (conv != null) {
                return Result.success(conv);
            }
        }

        // 不存在，创建新会话
        String convId = UUID.randomUUID().toString().replace("-", "");
        Conversation conversation = new Conversation();
        conversation.setConversationId(convId);
        conversation.setType(1); // 私聊
        this.save(conversation);

        // 添加成员
        ConversationMember m1 = new ConversationMember();
        m1.setConversationId(convId);
        m1.setUserId(userId1);
        m1.setRole(1);
        memberMapper.insert(m1);

        ConversationMember m2 = new ConversationMember();
        m2.setConversationId(convId);
        m2.setUserId(userId2);
        m2.setRole(1);
        memberMapper.insert(m2);

        return Result.success(conversation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Conversation> createGroupConversation(String ownerId, String title, List<String> memberIds) {
        String convId = UUID.randomUUID().toString().replace("-", "");
        Conversation conversation = new Conversation();
        conversation.setConversationId(convId);
        conversation.setType(2); // 群聊
        conversation.setTitle(title);
        conversation.setOwnerId(ownerId);
        this.save(conversation);

        // 添加群主
        ConversationMember owner = new ConversationMember();
        owner.setConversationId(convId);
        owner.setUserId(ownerId);
        owner.setRole(3); // 群主
        memberMapper.insert(owner);

        // 添加其他成员
        if (memberIds != null) {
            for (String memberId : memberIds) {
                if (memberId.equals(ownerId)) continue;
                ConversationMember member = new ConversationMember();
                member.setConversationId(convId);
                member.setUserId(memberId);
                member.setRole(1); // 普通成员
                memberMapper.insert(member);
            }
        }

        return Result.success(conversation);
    }

    @Override
    public Result<List<Conversation>> getUserConversations(String userId) {
        List<String> convIds = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        if (convIds.isEmpty()) {
            return Result.success(List.of());
        }

        List<Conversation> conversations = this.list(new LambdaQueryWrapper<Conversation>()
                .in(Conversation::getConversationId, convIds)
                .orderByDesc(Conversation::getUpdateTime));

        return Result.success(conversations);
    }

    /**
     * 详情版本：拼装 {@link ConversationListItemDTO} 列表。
     *
     * <p>实现采用「先按 conversationId 拉所有需要的子表，再循环组装」的简单做法。
     * 私聊量级不大时性能足够，将来量级上来可以改成一次性 IN 查询 + 内存聚合。
     */
    @Override
    public Result<List<ConversationListItemDTO>> getUserConversationsWithDetail(String userId) {
        List<Conversation> conversations = getUserConversations(userId).getData();
        if (conversations == null || conversations.isEmpty()) {
            return Result.success(List.of());
        }

        List<ConversationListItemDTO> items = new ArrayList<>(conversations.size());
        for (Conversation conv : conversations) {
            ConversationListItemDTO item = new ConversationListItemDTO();
            BeanUtils.copyProperties(conv, item);

            // 1) 私聊对方 uid（仅 type=1 时填）
            if (Integer.valueOf(1).equals(conv.getType())) {
                ConversationMember other = memberMapper.selectOne(new LambdaQueryWrapper<ConversationMember>()
                        .eq(ConversationMember::getConversationId, conv.getConversationId())
                        .ne(ConversationMember::getUserId, userId)
                        .last("limit 1"));
                if (other != null) {
                    item.setOtherUserId(other.getUserId());
                }
            }

            // 2) 最后一条非删除消息
            Message lastMsg = messageMapper.selectOne(new LambdaQueryWrapper<Message>()
                    .eq(Message::getConversationId, conv.getConversationId())
                    .eq(Message::getDeleted, 0)
                    .orderByDesc(Message::getCreateTime)
                    .last("limit 1"));
            item.setLastMessage(lastMsg);

            // 3) 未读数 = 对方在该会话发的非删除消息总数 - 我已读的"该会话中对方发的消息"数
            //    没有消息时直接 0。
            Long total = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                    .eq(Message::getConversationId, conv.getConversationId())
                    .ne(Message::getFromUser, userId)
                    .eq(Message::getDeleted, 0));
            long unread = 0L;
            if (total != null && total > 0) {
                List<String> theirMsgIds = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                        .select(Message::getMessageId)
                        .eq(Message::getConversationId, conv.getConversationId())
                        .ne(Message::getFromUser, userId)
                        .eq(Message::getDeleted, 0))
                        .stream().map(Message::getMessageId).collect(Collectors.toList());
                long read = 0L;
                if (!theirMsgIds.isEmpty()) {
                    Long readCount = readStatusMapper.selectCount(new LambdaQueryWrapper<MessageReadStatus>()
                            .eq(MessageReadStatus::getUserId, userId)
                            .eq(MessageReadStatus::getReadFlag, 1)
                            .in(MessageReadStatus::getMessageId, theirMsgIds));
                    read = readCount == null ? 0L : readCount;
                }
                unread = Math.max(0L, total - read);
            }
            item.setUnreadCount(unread);

            items.add(item);
        }

        // 4) 按最后消息时间倒序，无消息的排末尾
        items.sort(Comparator.comparing(
                (ConversationListItemDTO it) -> {
                    Message m = it.getLastMessage();
                    return m == null ? null : m.getCreateTime();
                },
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return Result.success(items);
    }
}
