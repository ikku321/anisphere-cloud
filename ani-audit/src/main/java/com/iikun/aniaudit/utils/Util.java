package com.iikun.aniaudit.utils;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核模块小工具集合
 */
public class Util {

    private Util() {}

    /** 角色：管理员 */
    public static final int ROLE_ADMIN = 0;
    /** 角色：审核员 */
    public static final int ROLE_AUDITOR = 3;

    /**
     * 判断用户权限是否属于管理员权限。
     *
     * @param roole 权限代码（0=管理员）
     * @return true 表示是管理员
     * @deprecated 方法名拼写错误（roole）。新代码请改用 {@link #isAdmin(Integer)}。
     */
    @Deprecated
    public static boolean isUserRoole(Integer roole) {
        return isAdmin(roole);
    }

    /** 是否管理员（role == 0）。null 视为否。 */
    public static boolean isAdmin(Integer role) {
        return role != null && role == ROLE_ADMIN;
    }

    /**
     * 是否为后台 staff（管理员或审核员）。
     *
     * 用于「读」类管理端接口（任务列表、审核记录、统计等）：
     * 这些数据审核员需要看到才能开展工作；写操作仍应单独用 {@link #isAdmin}。
     */
    public static boolean isStaff(Integer role) {
        return role != null && (role == ROLE_ADMIN || role == ROLE_AUDITOR);
    }

}
