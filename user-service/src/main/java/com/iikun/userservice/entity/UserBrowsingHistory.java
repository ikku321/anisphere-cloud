package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19 23:06
 * version 1.0.0
 * msg: 用户浏览记录表实体类
 */
@Data
@TableName("user_browsing_history")
public class UserBrowsingHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID（关联user.id） */
    @TableField("user_id")
    private Long userId;

    /** 内容类型(0视频 1漫画 2评论 3其他) */
    @TableField("target_type")
    private Integer targetType;

    /** 目标内容ID */
    @TableField("target_id")
    private Long targetId;

    /** 浏览时间 */
    @TableField(value = "view_time", fill = FieldFill.INSERT)
    private LocalDateTime viewTime;
}

