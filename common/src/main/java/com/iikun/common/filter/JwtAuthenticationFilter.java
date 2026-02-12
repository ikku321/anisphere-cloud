package com.iikun.common.filter;

import com.iikun.common.context.UserContext;
import com.iikun.common.model.LoginUser;
import com.iikun.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * author iikun
 * time 2025/9/19 21:58
 * version 1.0.0
 * msg: JWT 认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> WHITE_LIST = List.of(
            "/user/login",
            "/user/register"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        log.info("访问路径: {}", uri);

        // 白名单直接放行
        if (WHITE_LIST.contains(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401, \"message\":\"缺少或无效的Token\"}");
            return;
        }

        String token = authHeader.substring(7);
        log.info("token: {}", token);

        try {
            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Token无效");
            }

            // 解析 token 获取用户 id
            String userId = jwtUtil.getSubject(token);
            if (userId == null) {
                throw new RuntimeException("Token解析失败，缺少 subject");
            }
            log.info("解析 token 获取到的用户 id: {}", userId);

            // 放入 SecurityContextHolder
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 获取Claims内所有信息
            Claims allClaims = jwtUtil.getAllClaims(token);
            String uid = allClaims.get("userId", String.class);
            String username = allClaims.get("username", String.class);
            String role = allClaims.get("role", String.class);

            // 存入UserContext
            LoginUser loginUser = new LoginUser();
            loginUser.setUid(uid);
            loginUser.setUsername(username);
            loginUser.setRole(role);
            // 将LoginUser存入ContextUser
            UserContext.setUser(loginUser);

            // 继续执行请求
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT 解析异常: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401, \"message\":\"Token无效或已过期\"}");
        }
    }
}

