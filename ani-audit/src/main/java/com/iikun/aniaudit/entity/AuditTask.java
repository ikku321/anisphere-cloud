package com.iikun.aniaudit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核任务表实体类（audit_task）
 */
@Data
@Schema(description = "审核任务表实体")
public class AuditTask implements Serializable {

    /**
     * 自增主键（内部使用）
     */
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 视频ID（业务ID，对应 video.video_id）
     */
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 任务分配时间
     */
    @Schema(description = "任务分配时间")
    private LocalDateTime assignTime;

    /**
     * 任务状态：
     * 0 - 待审核
     * 1 - 审核中
     * 2 - 已完成
     */
    @Schema(description = "任务状态：0待审 1进行中 2完成")
    private Integer status;


    @Schema(description = "审核员ID")
    private String auditorId;
}
