package com.iikun.anivideo;

import com.iikun.common.base.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 视频服务模块
 */
@EnableFeignClients
@MapperScan("com.iikun.anivideo.mapper")
@SpringBootApplication
public class AniVideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniVideoApplication.class, args);
        System.out.println("视频服务模块已启动...");
    }

}
