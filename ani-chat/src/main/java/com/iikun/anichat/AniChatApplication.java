package com.iikun.anichat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 消息模块
 */
@SpringBootApplication
public class AniChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniChatApplication.class, args);
        System.out.println("-----9096----> 消息模块已启动...");
    }

}
