package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.ChatHistoryClear;
import com.iikun.common.base.Result;

/**
 * 聊天记录清除请求业务逻辑接口
 */
public interface ChatHistoryClearService extends IService<ChatHistoryClear> {

    /**
     * 记录清除聊天记录请求
     *
     * @param requester       发起人 ID
     * @param conversationId 会话 ID
     * @param scope           范围：self/all
     * @return 结果
     */
    Result<Void> recordClearRequest(String requester, String conversationId, String scope);
}
