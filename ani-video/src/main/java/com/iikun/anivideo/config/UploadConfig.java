package com.iikun.anivideo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:文件上传配置
 */
@Configuration
@ConfigurationProperties(prefix = "file")
@Data
public class UploadConfig {

    private String uploadDir;

}