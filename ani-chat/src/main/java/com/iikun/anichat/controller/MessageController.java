package com.iikun.anichat.controller;

import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.dto.SendMessageDTO;
import com.iikun.anichat.entity.dto.UserDTO;
import com.iikun.anichat.fegin.client.UserFeignClient;
import com.iikun.anichat.service.MessageReadStatusService;
import com.iikun.anichat.service.MessageReportService;
import com.iikun.anichat.service.MessageService;
import com.iikun.common.base.Result;
import com.iikun.common.context.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制层
 */
@RestController
@RequestMapping("/chat/message")
@RequiredArgsConstructor
@Tag(name = "消息管理", description = "处理消息的发送、历史查询、撤回等操作")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "发送消息", description = "向指定会话发送文本、图片或文件消息")
    @PostMapping("/send")
    public Result<Message> send(@RequestBody SendMessageDTO sendDTO) {
        String fromUserId = UserContext.getUser().getUid();
        return messageService.sendMessage(fromUserId, sendDTO);
    }

    @Operation(summary = "获取历史消息", description = "分页获取指定会话的历史消息记录")
    @GetMapping("/history")
    public Result<List<Message>> getHistory(@RequestParam String conversationId,
                                            @RequestParam(required = false) String lastMessageId,
                                            @RequestParam(defaultValue = "20") Integer pageSize) {
        return messageService.getHistoryMessages(conversationId, lastMessageId, pageSize);
    }

    /**
     * 撤回消息
     *
     * @param messageId 消息 ID
     * @return 结果
     */
    @PostMapping("/recall")
    @Operation(summary = "撤回消息", description = "撤回自己发送的消息（2分钟内）或由管理员撤回")
    public Result<Void> recallMessage(@RequestParam String messageId) {
        String userId = UserContext.getUser().getUid();
        return messageService.recallMessage(userId, messageId);
    }

    /**
     * 删除消息
     *
     * @param messageId 消息 ID
     * @return 结果
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除消息", description = "用户逻辑删除自己的消息")
    public Result<Void> deleteMessage(@RequestParam String messageId) {
        String userId = UserContext.getUser().getUid();
        return messageService.deleteMessage(userId, messageId);
    }

    /**
     * 标记会话已读
     *
     * @param conversationId 会话 ID
     * @return 结果
     */
    @PostMapping("/read")
    @Operation(summary = "标记已读", description = "将指定会话内的所有消息标记为已读")
    public Result<Void> markRead(@RequestParam String conversationId) {
        String userId = UserContext.getUser().getUid();
        return messageService.markRead(userId, conversationId);
    }
}
