package com.iikun.common.context;


import com.iikun.common.model.LoginUser;

/**
 * author iikun
 * time 2026/2/5 1:00
 * version 1.0.0
 * msg: 当前登录用户信息
 */
public final class UserContext {

    private static final ThreadLocal<LoginUser> USER_HOLDER = new ThreadLocal<>();

    public static void setUser(LoginUser user) {
        USER_HOLDER.set(user);
    }

    public static LoginUser getUser() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
