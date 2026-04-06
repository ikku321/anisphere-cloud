package com.iikun.anichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.MessageReadStatus;
import com.iikun.anichat.mapper.MessageMapper;
import com.iikun.anichat.mapper.MessageReadStatusMapper;
import com.iikun.anichat.service.MessageReadStatusService;
import com.iikun.common.base.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息已读状态服务实现类
 *
 * @author iikun
 */
@Service
@RequiredArgsConstructor
public class MessageReadStatusServiceImpl extends ServiceImpl<MessageReadStatusMapper, MessageReadStatus> implements MessageReadStatusService {

    private final MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> markAsRead(String userId, String messageId) {
        MessageReadStatus status = this.getOne(new LambdaQueryWrapper<MessageReadStatus>()
                .eq(MessageReadStatus::getUserId, userId)
                .eq(MessageReadStatus::getMessageId, messageId));

        if (status == null) {
            status = new MessageReadStatus();
            status.setUserId(userId);
            status.setMessageId(messageId);
            status.setReadFlag(1);
            status.setReadTime(LocalDateTime.now());
            this.save(status);
        } else if (status.getReadFlag() == 0) {
            status.setReadFlag(1);
            status.setReadTime(LocalDateTime.now());
            this.updateById(status);
        }

        return Result.success(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> markConversationAsRead(String userId, String conversationId) {
        // 1. 查找会话内所有未读消息
        // 这里的逻辑稍微复杂一些，因为我们需要知道哪些消息是发给该用户的
        // 在私聊或群聊中，所有不是该用户发送的消息，该用户都应该有已读/未读状态
        
        // 找到该会话中所有不是该用户发送的消息
        List<String> unreadMsgIds = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .eq(Message::getConversationId, conversationId)
                .ne(Message::getFromUser, userId))
                .stream().map(Message::getMessageId).collect(Collectors.toList());

        if (unreadMsgIds.isEmpty()) {
            return Result.success(null);
        }

        // 2. 更新这些消息的状态为已读
        for (String msgId : unreadMsgIds) {
            this.markAsRead(userId, msgId);
        }

        return Result.success(null);
    }
}
