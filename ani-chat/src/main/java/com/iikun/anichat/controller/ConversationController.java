package com.iikun.anichat.controller;

import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.dto.UserDTO;
import com.iikun.anichat.fegin.client.UserFeignClient;
import com.iikun.anichat.service.ConversationService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会话控制层
 */
@RestController
@RequestMapping("/chat/conversation")
@RequiredArgsConstructor
@Tag(name = "会话管理", description = "处理私聊、群聊会话的创建与查询")
public class ConversationController {

    private final ConversationService conversationService;

    @Operation(summary = "创建或获取私聊会话", description = "获取两个用户之间的私聊会话，如果不存在则创建")
    @PostMapping("/private")
    public Result<Conversation> createPrivate(@RequestParam String otherUserId) {
        String userId = UserContext.getUser().getUid();
        return conversationService.createOrGetPrivateConversation(userId, otherUserId);
    }

    @Operation(summary = "获取当前用户的会话列表", description = "获取当前登录用户参与的所有会话列表")
    @GetMapping("/list")
    public Result<List<Conversation>> getMyList() {
        String userId = UserContext.getUser().getUid();
        return conversationService.getUserConversations(userId);
    }

    @Operation(summary = "创建群聊会话", description = "创建一个新的群聊会话")
    @PostMapping("/group")
    public Result<Conversation> createGroup(@RequestParam String title, @RequestBody List<String> memberIds) {
        String userId = UserContext.getUser().getUid();
        return conversationService.createGroupConversation(userId, title, memberIds);
    }

    @Operation(
            summary = "获取当前用户的会话列表（带详情）",
            description = "在普通会话基础上额外返回最后一条消息预览、未读数、私聊对方 uid，专供消息页私信 Tab"
    )
    @GetMapping("/list-with-detail")
    public Result<List<com.iikun.anichat.entity.dto.ConversationListItemDTO>> getMyConversationsWithDetail() {
        String userId = UserContext.getUser().getUid();
        return conversationService.getUserConversationsWithDetail(userId);
    }
}
