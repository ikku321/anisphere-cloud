package com.iikun.common.filter;

import com.iikun.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> WHITE_LIST = List.of(
            "/user/login",
            "/user/register",
            "/admin/admin-login",
            "/uploads/**"
    );

    private boolean isWhiteList(String uri) {
        return WHITE_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        log.info("访问路径: {}", uri);

        // 正确白名单判断
        if (isWhiteList(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Token 来源优先级：
        //   1. Authorization Header（API 接口走 Ktor / Axios，最常见）
        //   2. query 参数 ?token=xxx
        //      —— <img> / <video> / 视频播放器（Media Foundation、ExoPlayer、VLC）
        //         无法注入自定义 Header，只能把 token 放 URL 里。
        // 这与 ChatHandshakeInterceptor、DanmakuHandshakeInterceptor 已有的 query
        // token 模式保持一致。
        String token = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token == null || token.isBlank()) {
            String queryToken = request.getParameter("token");
            if (queryToken != null && !queryToken.isBlank()) {
                token = queryToken;
            }
        }

        if (token == null || token.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401, \"message\":\"缺少或无效的Token\"}");
            return;
        }

        try {
            if (!jwtUtil.validateToken(token)) {
                throw new ServletException("Token无效");
            }

            String userId = jwtUtil.getSubject(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT 解析异常: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401, \"message\":\"Token无效或已过期\"}");
        }
    }
}

