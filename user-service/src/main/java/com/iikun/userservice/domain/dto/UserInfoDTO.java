package com.iikun.userservice.domain.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * author iikun
 * time 2025/9/19 22:47
 * version 1.0.0
 * msg: 展示用户信息实体类
 */
@Data
public class UserInfoDTO {
    private String userId;
    private String username;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private String email;
    private Integer exp;
    private Integer level;
    private Integer status;
    private String role;
    private Integer followersCount;
    private Integer followingCount;
    private LocalDate birthday;
    private String bio;
    private String gender;
}