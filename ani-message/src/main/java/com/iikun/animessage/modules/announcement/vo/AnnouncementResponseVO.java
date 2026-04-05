package com.iikun.animessage.modules.announcement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公告响应 VO
 */
@Data
@Schema(description = "公告响应参数")
public class AnnouncementResponseVO {

    @Schema(description = "公告ID")
    private String announcementId;

    @Schema(description = "公告标题")
    private String title;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "作者ID")
    private String authorId;

    @Schema(description = "是否发布（0-草稿，1-发布）")
    private Integer isPublished;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
