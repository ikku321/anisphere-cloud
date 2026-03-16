package com.iikun.anivideo.entity;

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
 * 视频统计信息表
 * 表：video_stat
 * 记录视频的播放、点赞、分享、评论等统计数据
 */
@Data
@TableName("video_stat")
@Schema(description = "视频统计信息")
public class VideoStatEntity {

    /**
     * 视频ID
     * 对应 video.video_id
     */
    @TableId("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 播放次数
     */
    @TableField("play_count")
    @Schema(description = "播放次数")
    private Long playCount;

    /**
     * 点赞数
     */
    @TableField("like_count")
    @Schema(description = "点赞数")
    private Long likeCount;

    /**
     * 分享数
     */
    @TableField("share_count")
    @Schema(description = "分享数")
    private Long shareCount;

    /**
     * 评论数
     */
    @TableField("comment_count")
    @Schema(description = "评论数")
    private Long commentCount;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
