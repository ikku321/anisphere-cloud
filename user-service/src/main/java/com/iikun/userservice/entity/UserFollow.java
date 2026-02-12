package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19
 * version 1.0.0
 * msg: 用户关注关系表实体类
 */
@Data
@TableName("user_follow")
public class UserFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 粉丝用户ID（关联user.id） */
    @TableField("follower_id")
    private Long followerId;

    /** 被关注用户ID（关联user.id） */
    @TableField("following_id")
    private Long followingId;

    /** 关注时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
