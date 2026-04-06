package com.iikun.anichat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 消息模块
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("com.iikun.anichat.mapper")
@ComponentScan(
        basePackages = {"com.iikun.anichat", "com.iikun.common"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.iikun.common.config.SecurityConfig.class} // 排除 common 模块的通用安全配置，以使用当前模块的自定义安全配置
        )
)
public class AniChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniChatApplication.class, args);
        System.out.println("-----9096----> 消息模块已启动...");
    }

}
