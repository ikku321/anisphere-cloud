package com.iikun.anivideo;

import com.iikun.common.base.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 视频服务模块
 */
@EnableFeignClients
@MapperScan("com.iikun.anivideo.mapper")
@SpringBootApplication
@ComponentScan(
        basePackages = {"com.iikun.anivideo", "com.iikun.common"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.iikun.common.config.SecurityConfig.class} // 排除 common 模块的通用安全配置，以使用当前模块的自定义安全配置
        )
)
public class AniVideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniVideoApplication.class, args);
        System.out.println("-----9091----> 视频服务模块已启动...");
    }

}
