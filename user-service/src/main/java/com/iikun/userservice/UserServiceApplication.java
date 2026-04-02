package com.iikun.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 用户服务模块
 */
@SpringBootApplication(scanBasePackages = {"com.iikun.userservice", "com.iikun.common"})
@MapperScan("com.iikun.userservice.mapper")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("-----9090----> 用户服务模块已启动...");
    }

}
