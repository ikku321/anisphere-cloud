package com.iikun.anichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.dto.SendMessageDTO;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 消息服务接口
 *
 * @author iikun
 */
public interface MessageService extends IService<Message> {

    /**
     * 发送消息
     *
     * @param fromUserId 发送者 ID
     * @param sendDTO    消息内容
     * @return 发送成功的消息
     */
    Result<Message> sendMessage(String fromUserId, SendMessageDTO sendDTO);

    /**
     * 获取会话的历史消息
     *
     * @param conversationId 会话 ID
     * @param lastMessageId  最后一条消息 ID (分页用)
     * @param pageSize      每页数量
     * @return 消息列表
     */
    Result<List<Message>> getHistoryMessages(String conversationId, String lastMessageId, Integer pageSize);

    /**
     * 撤回消息
     *
     * @param operatorId 执行操作的用户 ID
     * @param messageId  消息 ID
     * @return 结果
     */
    Result<Void> recallMessage(String operatorId, String messageId);

    /**
     * 逻辑删除消息（仅对当前用户不可见或全局删除）
     *
     * @param userId    操作用户 ID
     * @param messageId 消息 ID
     * @return 结果
     */
    Result<Void> deleteMessage(String userId, String messageId);

    /**
     * 批量标记消息为已读
     *
     * @param userId         用户 ID
     * @param conversationId 会话 ID
     * @return 结果
     */
    Result<Void> markRead(String userId, String conversationId);
}
