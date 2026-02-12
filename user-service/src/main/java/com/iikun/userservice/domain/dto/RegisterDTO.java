package com.iikun.userservice.domain.dto;

import jakarta.validation.Valid;
import lombok.Data;

/**
 * author iikun
 * time 2025/9/17 16:48
 * version 1.0.0
 * msg:
 */
@Data
public class RegisterDTO {
    // 用户名称
    private String username;
    // 用户号码
    private String phone;
    // 用户密码
    private String password;
    // 用户邮箱
    private String email;
}
