package com.iikun.anivideo.entity.DTO;

import lombok.Data;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Data
public class UserDTO {
    private String role;
    private String gender;
    private long level;
    private String userId;
    private long followingCount;
    private String phone;
    private String nickname;
    private long followersCount;
    private long exp;
    private String email;
    private String username;
    private long status;
}