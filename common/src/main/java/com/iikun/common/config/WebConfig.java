package com.iikun.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 通用 Web MVC 配置占位类。
 * <p>
 * 历史版本曾在此把 /uploads/** 映射到 file:///D:/anivideo/uploads/,但 common 被所有微服务依赖,
 * 这种硬编码会让 Linux 服务器部署后 100% 触发 404。已下放到各业务服务自己注册:
 * - user-service: {@code com.iikun.userservice.config.UserUploadStaticConfig}
 * - ani-video:    {@code com.iikun.anivideo.config.VideoUploadStaticConfig}
 * <p>
 * 各服务通过自己的 {@code file.upload-dir} 配置项把磁盘目录暴露成 /uploads/{user|video}/** URL,
 * 网关再按命名空间分流。
 *
 * author iikun
 * time 2026/2/13 0:35
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 不再注册任何全局静态资源,各服务自行决定
}
