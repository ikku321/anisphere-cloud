package com.iikun.common.config;

import com.iikun.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author iikun
 * time 2025/9/18 1:17
 * version 1.0.0
 * msg:
 */
@Configuration
public class JwtConfig {

    @Bean
    public JwtUtil jwtUtil() {
        String secret = "QkNzv3tkvKcVf7yMkbFvX2d8yFZnFZzB8k0lX5mI+OQ=";
        long expireSeconds = 604800;
        return new JwtUtil(secret, expireSeconds);
    }

}
