package com.iikun.animessage.modules.notification.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知响应 VO
 */
@Data
@Schema(description = "通知响应参数")
public class NotificationResponseVO {

    @Schema(description = "通知业务ID")
    private String notificationId;

    @Schema(description = "接收者用户ID")
    private String targetUser;

    @Schema(description = "通知分类")
    private String category;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String content;

    @Schema(description = "是否已读（0-未读，1-已读）")
    private Integer isRead;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
