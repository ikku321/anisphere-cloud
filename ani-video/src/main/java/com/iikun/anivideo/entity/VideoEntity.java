package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 视频主表
 * <p>
 * 对应数据库表：video
 */
@Data
@TableName("video")
@Schema(description = "视频信息表")
public class VideoEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    /**
     * 视频唯一ID
     */
    @TableField("video_id")
    @Schema(description = "视频唯一ID")
    private String videoId;

    /**
     * 发布者ID
     */
    @TableField("user_id")
    @Schema(description = "发布者ID")
    private String userId;

    /**
     * 视频标题
     */
    @NotBlank(message = "视频标题不能为空")
    @TableField("title")
    @Schema(description = "视频标题")
    private String title;

    /**
     * 视频简介
     */
    @NotBlank(message = "视频简介不能为空")
    @TableField("description")
    @Schema(description = "视频简介")
    private String description;

    /**
     * 视频封面
     */
    @NotBlank(message = "视频封面不能为空")
    @TableField("cover_url")
    @Schema(description = "封面图片地址")
    private String coverUrl;

    /**
     * 视频播放地址
     */
    @NotBlank(message = "视频播放地址不能为空")
    @TableField("video_url")
    @Schema(description = "视频文件地址")
    private String videoUrl;

    /**
     * 视频时长（秒）
     */
    @TableField("duration")
    @Schema(description = "视频时长（秒）")
    private Integer duration;

    /**
     * 视频状态
     * 0 审核中
     * 1 正常
     * 2 隐藏
     * 3 违规
     * 4 付费
     */
    @TableField("status")
    @Schema(description = "视频状态")
    private Integer status;

    /**
     * 是否可见
     * 1 公开
     * 0 隐藏
     */
    @TableField("visible")
    @Schema(description = "是否公开")
    private Integer visible;

    /**
     * 视频价格
     */
    @TableField("price")
    @Schema(description = "视频价格")
    private BigDecimal price;

    /**
     * 审核状态
     * 0 待审核
     * 1 通过
     * 2 拒绝
     */
    @TableField("audit_status")
    @Schema(description = "审核状态")
    private Integer auditStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
