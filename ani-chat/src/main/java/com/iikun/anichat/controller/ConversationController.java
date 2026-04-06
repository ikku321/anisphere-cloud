package com.iikun.anichat.controller;

import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.dto.UserDTO;
import com.iikun.anichat.fegin.client.UserFeignClient;
import com.iikun.anichat.service.ConversationService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会话管理控制器
 *
 * @author iikun
 */
@Tag(name = "会话管理", description = "会话管理相关接口")
@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final UserFeignClient userFeignClient;

    /**
     * 获取当前用户的会话列表
     */
    @Operation(summary = "获取会话列表", description = "获取当前登录用户的所有聊天会话")
    @GetMapping("/list")
    public Result<List<Conversation>> getConversations() {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return conversationService.getUserConversations(userResult.getData().getUserId());
    }

    /**
     * 创建或进入私聊会话
     */
    @Operation(summary = "创建/获取私聊", description = "与指定用户建立私聊会话，若已存在则直接返回")
    @PostMapping("/private")
    public Result<Conversation> createPrivateConversation(@RequestParam String targetUserId) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return conversationService.createOrGetPrivateConversation(userResult.getData().getUserId(), targetUserId);
    }

    /**
     * 创建群聊会话
     */
    @Operation(summary = "创建群聊", description = "创建一个新的群聊会话")
    @PostMapping("/group")
    public Result<Conversation> createGroupConversation(@RequestParam String title, @RequestBody List<String> memberIds) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return conversationService.createGroupConversation(userResult.getData().getUserId(), title, memberIds);
    }
}
