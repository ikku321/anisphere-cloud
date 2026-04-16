package com.iikun.userservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class UploadUserConfig {

    private String uploadDir;

}
