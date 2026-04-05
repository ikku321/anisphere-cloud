package com.iikun.animessage.modules.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 公告查询 DTO
 */
@Data
@Schema(description = "公告查询参数")
public class AnnouncementQueryDTO {

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Schema(description = "关键词（标题/内容）")
    private String keyword;

    @Schema(description = "发布状态（0-草稿，1-发布）")
    private Integer isPublished;
}
