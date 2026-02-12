package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19
 * version 1.0.0
 * msg: 用户黑名单表实体类
 */
@Data
@TableName("user_blacklist")
public class UserBlacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID（关联user.id） */
    @TableField("user_id")
    private Long userId;

    /** 被拉黑的用户ID（关联user.id） */
    @TableField("blocked_user_id")
    private Long blockedUserId;

    /** 拉黑时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
