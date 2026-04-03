package com.iikun.aniaudit.utils;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public class Util {

    /**
     * 判断用户权限是否属于管理员权限
     *
     * @param roole 权限代码
     * @return 返回结果
     */
    public static boolean isUserRoole(Integer roole) {
        return roole == 0;
    }

}
