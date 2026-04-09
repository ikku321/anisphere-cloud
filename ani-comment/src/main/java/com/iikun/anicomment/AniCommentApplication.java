package com.iikun.anicomment;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 评论服务模块
 */
@EnableFeignClients
@SpringBootApplication
@RequiredArgsConstructor
public class AniCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniCommentApplication.class, args);
        System.out.println("-----9092----> 评论服务模块已启动...");
    }

}
