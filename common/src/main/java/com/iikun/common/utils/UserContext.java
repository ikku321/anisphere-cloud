package com.iikun.common.utils;

/**
 * author iikun
 * time 2025/9/18 23:45
 * version 1.0.0
 * msg:
 */
public class UserContext {
    private static final ThreadLocal<String> userHolder = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userHolder.set(userId);
    }

    public static String getUserId() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}

