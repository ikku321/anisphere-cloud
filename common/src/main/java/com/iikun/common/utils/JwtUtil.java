package com.iikun.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.iikun.common.utils.UserContext.getUserId;

/**
 * author iikun
 * time 2025/9/18 1:04
 * version 1.0.0
 * msg: JwtUtil工具类
 */
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expireSeconds;

    public JwtUtil(String base64Secret, long expireSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        this.expireSeconds = expireSeconds;
    }

    /**
     * 生成 token（把常用用户信息放到 claims 中）
     */
    public String generateToken(String subject, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder()
                .setClaims(extraClaims == null ? new HashMap<>() : extraClaims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expireSeconds, ChronoUnit.SECONDS)))
                .signWith(secretKey, SignatureAlgorithm.HS256);
        return builder.compact();
    }

    /**
     * 生成简单 token（只给 subject）
     */
    public String generateToken(String subject) {
        return generateToken(subject, null);
    }

    /**
     * 解析并获取全部 Claims（会在过期或签名异常时抛异常）
     */
    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * 获取特定 claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 获取 subject（通常是 userId）
     */
    public String getSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Date exp = getClaim(token, Claims::getExpiration);
            return exp.before(new Date());
        } catch (JwtException e) {
            // 如果解析出错（签名/格式/其他），视为无效/过期
            return true;
        }
    }

    /**
     * 验证 token（包含签名和过期校验）
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 刷新 token
     */
    public String refreshToken(String token) {
        Claims claims = getAllClaims(token);
        String subject = claims.getSubject();

        Map<String, Object> newClaims = new HashMap<>();
        newClaims.put("uid", claims.get("uid"));
        newClaims.put("role", claims.get("role"));

        return generateToken(subject, newClaims);
    }
}
