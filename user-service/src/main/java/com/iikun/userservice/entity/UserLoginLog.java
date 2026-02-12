package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19
 * version 1.0.0
 * msg: 用户登录日志表实体类
 */
@Data
@TableName("user_login_log")
public class UserLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID（关联user.id） */
    @TableField("user_id")
    private Long userId;

    /** 登录时间 */
    @TableField(value = "login_time", fill = FieldFill.INSERT)
    private LocalDateTime loginTime;

    /** 登录IP地址 */
    @TableField("login_ip")
    private String loginIp;

    /** 登录设备信息 */
    private String device;

    /** 登录状态(0失败 1成功) */
    private Integer status = 1; // 默认成功
}
