package com.iikun.anichat.controller;

import com.iikun.anichat.entity.Message;
import com.iikun.anichat.entity.dto.SendMessageDTO;
import com.iikun.anichat.entity.dto.UserDTO;
import com.iikun.anichat.fegin.client.UserFeignClient;
import com.iikun.anichat.service.MessageReadStatusService;
import com.iikun.anichat.service.MessageReportService;
import com.iikun.anichat.service.MessageService;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息管理控制器
 *
 * @author iikun
 */
@Tag(name = "消息管理", description = "消息管理相关接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageReadStatusService readStatusService;
    private final MessageReportService reportService;
    private final UserFeignClient userFeignClient;

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息", description = "向指定会话发送消息")
    @PostMapping("/send")
    public Result<Message> sendMessage(@RequestBody SendMessageDTO sendDTO) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return messageService.sendMessage(userResult.getData().getUserId(), sendDTO);
    }

    /**
     * 获取历史消息
     */
    @Operation(summary = "获取历史消息", description = "分页获取指定会话的历史消息")
    @GetMapping("/history")
    public Result<List<Message>> getHistoryMessages(@RequestParam String conversationId,
                                                   @RequestParam(required = false) String lastMessageId,
                                                   @RequestParam(defaultValue = "20") Integer pageSize) {
        return messageService.getHistoryMessages(conversationId, lastMessageId, pageSize);
    }

    /**
     * 撤回消息
     */
    @Operation(summary = "撤回消息", description = "撤回已发送的消息")
    @PostMapping("/recall/{messageId}")
    public Result<Void> recallMessage(@PathVariable String messageId) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return messageService.recallMessage(userResult.getData().getUserId(), messageId);
    }

    /**
     * 标记消息为已读
     */
    @Operation(summary = "标记消息为已读", description = "标记单条消息为已读状态")
    @PostMapping("/read/{messageId}")
    public Result<Void> markAsRead(@PathVariable String messageId) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return readStatusService.markAsRead(userResult.getData().getUserId(), messageId);
    }

    /**
     * 标记会话为已读
     */
    @Operation(summary = "标记会话为已读", description = "标记指定会话内所有消息为已读状态")
    @PostMapping("/read/conversation/{conversationId}")
    public Result<Void> markConversationAsRead(@PathVariable String conversationId) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return readStatusService.markConversationAsRead(userResult.getData().getUserId(), conversationId);
     }

    /**
     * 举报消息
     */
    @Operation(summary = "举报消息", description = "举报违规消息")
    @PostMapping("/report/{messageId}")
    public Result<Void> reportMessage(@PathVariable String messageId, @RequestParam String reason) {
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult.getCode() != 200) {
            return Result.failed("未登录或获取用户信息失败");
        }
        return reportService.reportMessage(userResult.getData().getUserId(), messageId, reason);
    }
 }
