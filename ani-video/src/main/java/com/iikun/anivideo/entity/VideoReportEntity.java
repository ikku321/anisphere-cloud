package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 * 视频举报记录表
 * 表：video_report
 * 记录用户对视频的违规举报
 */
@Data
@TableName("video_report")
@Schema(description = "视频举报记录")
public class VideoReportEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "举报ID")
    private Long id;

    /**
     * 被举报视频ID
     */
    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 举报用户ID
     */
    @TableField("user_id")
    @Schema(description = "举报用户ID")
    private String userId;

    /**
     * 举报原因
     */
    @TableField("reason")
    @Schema(description = "举报原因")
    private String reason;

    /**
     * 处理状态
     * 0 待处理
     * 1 已处理
     */
    @TableField("status")
    @Schema(description = "处理状态")
    private Integer status;

    /**
     * 举报时间
     */
    @TableField("create_time")
    @Schema(description = "举报时间")
    private LocalDateTime createTime;
}
