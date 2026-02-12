package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/17 14:08
 * version 1.0.0
 * msg: 用户对应数据库表模型
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户唯一id（雪花算法/UUID） */
    @TableField("user_id")
    private String userId;

    /** 账号名称 */
    private String username;

    /** 加密密码（BCrypt/Argon2） */
    private String password;

    /** 用户昵称 */
    private String nickname;

    /** 手机号（可登录） */
    private String phone;

    /** 邮箱（可登录） */
    private String email;

    /** 性别(0未知 1男 2女) */
    private Integer gender;

    /** 用户简介 */
    private String bio;

    /** 生日 */
    private LocalDate birthday;

    /** 头像地址 */
    @TableField("avatar_url")
    private String avatarUrl;

    /** 粉丝数 */
    @TableField("followers_count")
    private Integer followersCount;

    /** 关注数 */
    @TableField("following_count")
    private Integer followingCount;

    /** 在线状态(0离线 1在线 2隐身) */
    @TableField("online_status")
    private Integer onlineStatus;

    /** 会员id（关联vip表） */
    @TableField("vip_id")
    private Long vipId;

    /** 账号状态(0正常 1禁言 2封禁 3注销中) */
    private Integer status;

    /** 用户角色(0管理员 1普通用户 2UP主 3审核员) */
    private Integer role;

    /** 虚拟币（豆子） */
    private Integer coins;

    /** 等级 */
    private Integer level;

    /** 经验值 */
    private Integer exp;

    /** 实名认证信息id */
    @TableField("id_card_id")
    private Long idCardId;

    /** 最后登录时间 */
    @TableField("last_login_at")
    private LocalDateTime lastLoginAt;

    /** 最后登录IP */
    @TableField("last_login_ip")
    private String lastLoginIp;

    /** 注册时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 最后更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
