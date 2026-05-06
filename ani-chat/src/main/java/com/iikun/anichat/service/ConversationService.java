package com.iikun.anichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.dto.ConversationListItemDTO;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 会话服务接口
 *
 * @author iikun
 */
public interface ConversationService extends IService<Conversation> {

    /**
     * 创建或获取私聊会话
     *
     * @param userId1 用户1 ID
     * @param userId2 用户2 ID
     * @return 会话信息
     */
    Result<Conversation> createOrGetPrivateConversation(String userId1, String userId2);

    /**
     * 创建群聊会话
     *
     * @param ownerId 群主 ID
     * @param title   群名称
     * @param memberIds 初始成员列表
     * @return 会话信息
     */
    Result<Conversation> createGroupConversation(String ownerId, String title, List<String> memberIds);

    /**
     * 获取用户的所有会话列表
     *
     * @param userId 用户 ID
     * @return 会话列表
     */
    Result<List<Conversation>> getUserConversations(String userId);

    /**
     * 获取当前用户的会话列表（含最后消息预览 + 未读数 + 私聊对方 uid）。
     *
     * <p>专供「消息页 - 私信 Tab」使用，避免前端为每个会话单独拉 history。
     *
     * @param userId 当前登录用户 ID
     * @return 会话详情列表，按 lastMessage.createTime 倒序，无消息会话排在末尾
     */
    Result<List<ConversationListItemDTO>> getUserConversationsWithDetail(String userId);
}
