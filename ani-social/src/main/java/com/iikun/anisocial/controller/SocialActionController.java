package com.iikun.anisocial.controller;

import com.iikun.anisocial.service.ChatHistoryClearService;
import com.iikun.anisocial.service.UserReportService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 社交行为控制器 (举报、记录清理等)
 */
@RestController // Spring MVC 控制器注解
@RequestMapping("/social/action") // 指定接口根路径
@Tag(name = "社交行为管理", description = "提供举报用户、清除聊天记录记录等社交辅助功能") // Swagger 文档标签
public class SocialActionController {

    @Resource // 注入依赖
    private UserReportService userReportService; // 用户举报服务

    @Resource // 注入依赖
    private ChatHistoryClearService chatHistoryClearService; // 聊天记录清除服务

    /**
     * 举报用户
     *
     * @param targetUser 被举报人 ID
     * @param reason     举报原因
     * @return 举报结果
     */
    @PostMapping("/report") // HTTP POST 方法映射
    @Operation(summary = "举报用户", description = "举报违规用户或行为") // Swagger 文档说明
    public Result<Void> submitReport(@RequestParam String targetUser, @RequestParam String reason) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return userReportService.submitReport(userId, targetUser, reason); // 执行举报逻辑
    }

    /**
     * 清除聊天记录请求记录
     *
     * @param conversationId 会话 ID
     * @param scope           清除范围：self/all
     * @return 记录结果
     */
    @PostMapping("/chat/clear") // HTTP POST 方法映射
    @Operation(summary = "清除聊天记录记录", description = "记录一次清除聊天记录的操作，实际清除逻辑通常异步进行") // Swagger 文档说明
    public Result<Void> recordChatClear(@RequestParam String conversationId, 
                                      @RequestParam(defaultValue = "self") String scope) {
        String userId = UserContext.getUser().getUid(); // 获取当前登录用户 ID
        return chatHistoryClearService.recordClearRequest(userId, conversationId, scope); // 执行记录逻辑
    }
}
