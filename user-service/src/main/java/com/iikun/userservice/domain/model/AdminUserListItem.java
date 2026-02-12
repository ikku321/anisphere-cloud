package com.iikun.userservice.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/12
 * version 1.0.0
 * msg: 管理端查询用户列表返回模型
 * <p>
 * 说明：
 * - 该模型用于管理端展示用户列表，避免直接返回 User 实体（实体包含 password 等敏感字段）。
 * - 后续拆分微服务时，建议将该模型放到 admin-service 或 user-service 的管理 API 子模块中。
 * </p>
 */
@Data
public class AdminUserListItem {

    /** user.id 自增主键 */
    private Long id;

    /** user.user_id 业务ID */
    private String userId;

    /** 账号名称 */
    private String username;

    /** 用户昵称 */
    private String nickname;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 头像地址 */
    private String avatarUrl;

    /** 账号状态(0正常 1禁言 2封禁 3注销中) */
    private Integer status;

    /** 用户角色(0管理员 1普通用户 2UP主 3审核员) */
    private Integer role;

    /** 注册时间 */
    private LocalDateTime createTime;

    /** 最近更新时间 */
    private LocalDateTime updateTime;
}
