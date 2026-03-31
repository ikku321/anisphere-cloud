package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频分片上传表
 * <p>
 * 对应数据库表：video_chunk
 * 用于存储大文件分片上传的临时数据
 */
@Data
@TableName("video_chunk")
@Schema(description = "视频分片上传表")
public class VideoChunkEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 上传任务ID
     * 一次上传任务的唯一标识
     */
    @TableField("upload_id")
    @Schema(description = "上传任务ID")
    private String uploadId;

    /**
     * 视频ID
     * 对应 video.video_id
     */
    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 分片序号
     * 从0开始的分片索引
     */
    @TableField("chunk_index")
    @Schema(description = "分片序号")
    private Integer chunkIndex;

    /**
     * 分片文件地址
     * 分片文件在服务器的存储路径
     */
    @TableField("chunk_path")
    @Schema(description = "分片文件地址")
    private String chunkPath;

    /**
     * 分片状态
     * 0 未上传
     * 1 已上传
     */
    @TableField("status")
    @Schema(description = "分片状态")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
