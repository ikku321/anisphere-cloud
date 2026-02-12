package com.iikun.common.filter;

import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author iikun
 * @version 1.0.0
 * @date 2025/10/17 17:16
 * @description JWT 拦截器（过滤器）
 * <p>
 * 该过滤器继承自 {@link OncePerRequestFilter}，在每次请求时执行一次，
 * 用于从请求头中解析出 JWT Token，并提取其中的用户 UID（或用户名等标识），
 * 然后将其存入 {@link HttpServletRequest} 属性，供后续 Controller 层直接使用。
 * </p>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * JWT 工具类，用于解析和验证 Token。
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 每次 HTTP 请求到达时都会触发此方法。
     *
     * @param request     HTTP 请求对象
     * @param response    HTTP 响应对象
     * @param filterChain 过滤器链
     * @throws ServletException 过滤器异常
     * @throws IOException      IO 异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 从请求头中获取 Authorization
        String token = request.getHeader("Authorization");

        // 判断请求头中是否携带了 Token 且符合 Bearer 规范
        if (token != null && token.startsWith("Bearer ")) {
            // 去掉 "Bearer " 前缀，只保留真正的 Token 部分
            token = token.substring(7);
            // 使用 JwtUtil 工具类解析 Token，提取出用户 UID（或用户名）
            String uid = jwtUtil.getSubject(token);
            // 将 UID 存入当前请求作用域，方便后续 Controller 直接通过 request.getAttribute("uid") 获取
            request.setAttribute("uid", uid);


        }

        // 放行请求，让请求继续往下执行
        filterChain.doFilter(request, response);
    }
}
