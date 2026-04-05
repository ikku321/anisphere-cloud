package com.iikun.animessage.modules.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 通知查询 DTO
 */
@Data
@Schema(description = "通知查询参数")
public class NotificationQueryDTO {

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Schema(description = "通知分类 (system/notice/message/activity)")
    private String category;

    @Schema(description = "是否已读（0-未读，1-已读）")
    private Integer isRead;

    @Schema(description = "接收者用户ID (可选，通常从上下文获取)")
    private String targetUser;
}
