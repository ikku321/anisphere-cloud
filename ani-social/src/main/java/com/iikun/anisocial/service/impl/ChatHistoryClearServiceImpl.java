package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.entity.ChatHistoryClear;
import com.iikun.anisocial.mapper.ChatHistoryClearMapper;
import com.iikun.anisocial.service.ChatHistoryClearService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 聊天记录清除请求业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志
@Service // Spring 服务类注解
public class ChatHistoryClearServiceImpl extends ServiceImpl<ChatHistoryClearMapper, ChatHistoryClear> implements ChatHistoryClearService {

    /**
     * 记录清除聊天记录请求
     *
     * @param requester       发起人 ID
     * @param conversationId 会话 ID
     * @param scope           范围：self/all
     * @return 操作结果
     */
    @Override
    public Result<Void> recordClearRequest(String requester, String conversationId, String scope) {
        // 1. 实例化清除记录对象
        ChatHistoryClear clear = new ChatHistoryClear(); // 创建对象
        clear.setRequester(requester); // 设置发起人 ID
        clear.setConversationId(conversationId); // 设置会话 ID
        clear.setClearTime(LocalDateTime.now()); // 设置当前清除时间
        clear.setScope(scope != null ? scope : "self"); // 设置清除范围，默认为 self

        // 2. 保存记录
        boolean saved = this.save(clear); // 执行插入操作
        return saved ? Result.success() : Result.failed("记录清除请求失败"); // 返回结果
    }
}
