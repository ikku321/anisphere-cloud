package com.iikun.aniaudit.entity.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Data
public class UserDTO {
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
