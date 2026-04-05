package com.iikun.animessage.modules.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 通知请求 DTO
 */
@Data
@Schema(description = "通知请求参数")
public class NotificationRequestDTO {

    @NotBlank(message = "接收者不能为空")
    @Schema(description = "接收者用户ID")
    private String targetUser;

    @NotBlank(message = "通知分类不能为空")
    @Schema(description = "通知分类 (system/notice/message/activity)")
    private String category;

    @NotBlank(message = "通知标题不能为空")
    @Schema(description = "通知标题")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    @Schema(description = "通知内容")
    private String content;
}
