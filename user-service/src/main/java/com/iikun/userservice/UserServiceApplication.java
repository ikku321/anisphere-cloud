package com.iikun.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 用户服务模块
 */
@SpringBootApplication()
@MapperScan("com.iikun.userservice.mapper")
@ComponentScan(
        basePackages = {"com.iikun.userservice", "com.iikun.common"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {com.iikun.common.config.SecurityConfig.class} // 排除 common 模块的通用安全配置，以使用当前模块的自定义安全配置
        )
)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("-----9090----> 用户服务模块已启动...");
    }

}
