package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19
 * version 1.0.0
 * msg: 用户收藏表实体类
 */
@Data
@TableName("user_collection")
public class UserCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID（关联user.id） */
    @TableField("user_id")
    private Long userId;

    /** 收藏类型(0视频 1漫画 2评论) */
    @TableField("target_type")
    private Integer targetType;

    /** 收藏目标ID */
    @TableField("target_id")
    private Long targetId;

    /** 收藏时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
