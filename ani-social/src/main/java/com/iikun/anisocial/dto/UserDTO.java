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
