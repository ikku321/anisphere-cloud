package com.iikun.animessage.modules.announcement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 公告请求 DTO
 */
@Data
@Schema(description = "公告请求参数")
public class AnnouncementRequestDTO {

    @NotBlank(message = "公告标题不能为空")
    @Schema(description = "公告标题")
    private String title;

    @NotBlank(message = "公告内容不能为空")
    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "是否发布（0-草稿，1-发布）")
    private Integer isPublished;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;
}
