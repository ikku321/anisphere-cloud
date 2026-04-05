package com.iikun.anisocial.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息 DTO，用于接收 user-service 返回的数据
 */
@Data // Lombok 注解：自动生成 Getter/Setter 等
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    private String userId; // 用户 ID

    private String username; // 用户名

    private String nickname; // 昵称

    private String avatarUrl; // 头像地址

    private String phone; // 手机号

    private String email; // 邮箱

    private Integer exp; // 经验值

    private Integer level; // 等级

    private Integer status; // 状态

    private String role; // 角色

    private Integer followersCount; // 粉丝数

    private Integer followingCount; // 关注数

    private LocalDate birthday; // 生日

    private String bio; // 个人简介

    private String gender; // 性别
}
