package com.iikun.animessage.modules.notification.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iikun.animessage.modules.notification.dto.NotificationQueryDTO;
import com.iikun.animessage.modules.notification.dto.NotificationRequestDTO;
import com.iikun.animessage.modules.notification.service.NotificationService;
import com.iikun.animessage.modules.notification.vo.NotificationResponseVO;
import com.iikun.common.base.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 通知管理控制器
 */
@Tag(name = "通知管理", description = "针对单用户或多用户的通知")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "分页查询我的通知")
    @GetMapping("/page")
    public Result<Page<NotificationResponseVO>> getNotificationPage(NotificationQueryDTO queryDTO) {
        return Result.success(notificationService.getNotificationPage(queryDTO));
    }

    @Operation(summary = "发送通知 (系统内部/管理员调用)")
    @PostMapping("/send")
    public Result<Void> sendNotification(@RequestBody @Valid NotificationRequestDTO requestDTO) {
        notificationService.sendNotification(requestDTO);
        return Result.success();
    }

    @Operation(summary = "标记通知为已读")
    @PutMapping("/{notificationId}/read")
    public Result<Void> markAsRead(@PathVariable String notificationId) {
        notificationService.markAsRead(notificationId);
        return Result.success();
    }

    @Operation(summary = "全部标记为已读")
    @PutMapping("/read-all")
    public Result<Void> markAllAsRead() {
        notificationService.markAllAsRead(null);
        return Result.success();
    }

    @Operation(summary = "获取未读通知数量")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount() {
        return Result.success(notificationService.getUnreadCount(null));
    }

    @Operation(summary = "删除通知")
    @DeleteMapping("/{notificationId}")
    public Result<Void> deleteNotification(@PathVariable String notificationId) {
        notificationService.deleteNotification(notificationId);
        return Result.success();
    }
}
