package com.iikun.animessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 通知 + 公告模块（Notification / Announcement / Admin）模块
 */
@SpringBootApplication
public class AniMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniMessageApplication.class, args);
        System.out.println("-----9094----> 通知/公告服务模块已启动...");
    }

}
