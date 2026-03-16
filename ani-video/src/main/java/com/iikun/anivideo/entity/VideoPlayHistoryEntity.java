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
 * 视频播放历史表
 * 用于记录用户观看视频的播放进度，实现断点续播
 * 表：video_play_history
 */
@Data
@TableName("video_play_history")
@Schema(description = "视频播放历史（断点续播）")
public class VideoPlayHistoryEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 视频ID
     */
    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 最近播放进度（秒）
     */
    @TableField("last_position")
    @Schema(description = "最近播放进度（秒）")
    private Integer lastPosition;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
