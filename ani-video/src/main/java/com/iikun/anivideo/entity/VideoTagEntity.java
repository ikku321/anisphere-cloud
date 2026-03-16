package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 视频标签关系表
 * <p>
 * 表：video_tag
 * 用于存储 视频 与 标签 的多对多关系
 */
@Data
@TableName("video_tag")
@Schema(description = "视频标签关系表")
public class VideoTagEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 视频ID
     * 对应 video.video_id
     */
    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 标签ID
     * 对应 tag.id
     */
    @TableField("tag_id")
    @Schema(description = "标签ID")
    private Long tagId;
}