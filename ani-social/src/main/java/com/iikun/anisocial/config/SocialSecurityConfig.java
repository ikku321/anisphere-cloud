package com.iikun.anisocial.config;

import com.iikun.anisocial.filter.UserContextFilter;
import com.iikun.common.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.annotation.Resource;

/**
 * 社交服务安全配置类
 */
@Configuration // Spring 配置类注解
@EnableWebSecurity // 开启 Web 安全支持
@EnableMethodSecurity // 开启方法级别的权限控制注解支持 (@PreAuthorize)
public class SocialSecurityConfig {

    @Resource // 注入 common 模块提供的 JWT 过滤器
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource // 注入当前模块自定义的上下文过滤器
    private UserContextFilter userContextFilter;

    /**
     * 配置安全过滤链
     *
     * @param http HttpSecurity 配置对象
     * @return 过滤链实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS 由网关统一处理，下游服务关闭，避免响应头重复
                .cors(AbstractHttpConfigurer::disable)
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(userContextFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

}
