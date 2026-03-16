package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 * 视频收益流水表
 * 表：video_income
 * 记录UP主的视频收益来源
 */
@Data
@TableName("video_income")
@Schema(description = "视频收益流水")
public class VideoIncomeEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "收益记录ID")
    private Long id;

    /**
     * 视频ID
     */
    @TableField("video_id")
    @Schema(description = "视频ID")
    private String videoId;

    /**
     * 收益归属用户（UP主）
     */
    @TableField("user_id")
    @Schema(description = "UP主用户ID")
    private String userId;

    /**
     * 收益类型
     * 1 平台分成
     * 2 广告收益
     * 3 付费购买
     */
    @TableField("income_type")
    @Schema(description = "收益类型")
    private Integer incomeType;

    /**
     * 收益金额
     */
    @TableField("amount")
    @Schema(description = "收益金额")
    private BigDecimal amount;

    /**
     * 收益时间
     */
    @TableField("create_time")
    @Schema(description = "收益入账时间")
    private LocalDateTime createTime;
}
