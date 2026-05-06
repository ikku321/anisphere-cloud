package com.iikun.anicomment;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * author iikun
 * 毕业设计题目 基于微服务的动漫系统社交系统设计与实现
 * module 评论服务模块
 */
@EnableFeignClients
@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(
        basePackages = {"com.iikun.anicomment", "com.iikun.common"},
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                // 1) common.SecurityConfig            - 避免和本模块的 SecurityConfig 冲突；
                // 2) common.handler.GlobalExceptionHandler
                //                                     - 与本模块同名 bean (globalExceptionHandler) 冲突，
                //                                       本模块版本多处理了 DataAccessException，更全；
                // 3) common.filter.JwtAuthenticationFilter
                //                                     - @Component 的 OncePerRequestFilter，会被 Spring Boot
                //                                       自动注册为 servlet filter，对所有请求强制鉴权且白名单
                //                                       只覆盖 user-service 路径，会把评论模块全部请求拦成 401。
                //                                       本模块鉴权交给网关+业务层 (UserContext.getUser() 判空)。
                // 4) common.filter.JwtFilter          - 同上，本模块用 UserContextFilter 解析 token，
                //                                       不需要再额外注册 request attribute 模式的 filter。
                classes = {
                        com.iikun.common.config.SecurityConfig.class,
                        com.iikun.common.handler.GlobalExceptionHandler.class,
                        com.iikun.common.filter.JwtAuthenticationFilter.class,
                        com.iikun.common.filter.JwtFilter.class,
                }
        )
)
public class AniCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniCommentApplication.class, args);
        System.out.println("-----9092----> 评论服务模块已启动...");
    }

}
