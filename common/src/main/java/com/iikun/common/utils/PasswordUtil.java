package com.iikun.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * author iikun
 * time 2025/9/17 16:56
 * version 1.0.0
 * msg: 密码加密认证
 */
public class PasswordUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /** 加密 */
    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /** 校验密码 */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
