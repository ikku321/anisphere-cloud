package com.iikun.anichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anichat.entity.MessageReadStatus;
import com.iikun.common.base.Result;

/**
 * 消息已读状态服务接口
 *
 * @author iikun
 */
public interface MessageReadStatusService extends IService<MessageReadStatus> {

    /**
     * 标记单条消息为已读
     *
     * @param userId    用户 ID
     * @param messageId 消息 ID
     * @return 结果
     */
    Result<Void> markAsRead(String userId, String messageId);

    /**
     * 标记会话内所有消息为已读
     *
     * @param userId         用户 ID
     * @param conversationId 会话 ID
     * @return 结果
     */
    Result<Void> markConversationAsRead(String userId, String conversationId);
}
