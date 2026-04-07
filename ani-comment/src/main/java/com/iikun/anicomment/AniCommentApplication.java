package com.iikun.anicomment;

import com.iikun.anicomment.Feign.UserFeignClient;
import com.iikun.anicomment.Feign.service.UserService;
import com.iikun.anicomment.entity.DTO.UserDTO;
import com.iikun.anicomment.handle.UserFeignFallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                classes = {com.iikun.common.config.SecurityConfig.class} // 排除 common 模块的通用安全配置，以使用当前模块的自定义安全配置
        )
)
public class AniCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AniCommentApplication.class, args);
        System.out.println("-----9092----> 评论服务模块已启动...");
    }

}
