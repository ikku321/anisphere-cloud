package com.iikun.aniaudit;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 审核模块
 */
@EnableFeignClients
@MapperScan("com.iikun.aniaudit.mapper")
@SpringBootApplication
public class AniAuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniAuditApplication.class, args);
        System.out.println("-----9093----> 审核模块已启动...");
    }

}
