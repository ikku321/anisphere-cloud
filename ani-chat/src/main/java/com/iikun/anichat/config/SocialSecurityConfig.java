package com.iikun.anichat.config;

import com.iikun.common.filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Configuration // Spring 配置类注解
@EnableWebSecurity // 开启 Web 安全支持
@EnableMethodSecurity // 开启方法级别的权限控制注解支持 (@PreAuthorize)
public class SocialSecurityConfig {

    @Resource // 注入 common 模块提供的 JWT 过滤器
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置安全过滤链
     *
     * @param http HttpSecurity 配置对象
     * @return 过滤链实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 禁用 CSRF 保护（微服务通常使用 Token 验证）
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/social/post/global-list", // 公开的动态列表允许匿名访问
                                "/swagger-ui/**",          // Swagger 文档资源
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll() // 允许直接访问
                        .anyRequest().authenticated() // 其他所有社交操作接口必须通过身份验证
                )
                // 在用户名密码过滤器之前添加 JWT 认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build(); // 返回构建好的过滤链
    }
}
