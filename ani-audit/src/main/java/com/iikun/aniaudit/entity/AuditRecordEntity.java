package com.iikun.aniaudit.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/4/3
 * version 1.0.0
 * msg: 审核记录表实体类（audit_record）
 */
@Data
@Schema(description = "审核记录表（记录审核员对视频的处理结果）")
public class AuditRecordEntity {

    /**
     * 自增主键（内部ID）
     */
    @Schema(description = "自增主键")
    private Long id;

    /**
     * 视频ID（关联 video.video_id）
     */
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 审核人ID（用户ID/雪花ID）
     */
    @Schema(description = "审核人ID")
    private String auditorId;

    /**
     * 审核结果：
     * 1 = 通过
     * 2 = 拒绝
     * 3 = 待复审（可扩展）
     */
    @Schema(description = "审核结果：1通过 2拒绝 3待复审")
    private Integer result;

    /**
     * 审核备注/意见
     */
    @Schema(description = "审核备注")
    private String comment;

    /**
     * 审核时间（数据库自动生成）
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
