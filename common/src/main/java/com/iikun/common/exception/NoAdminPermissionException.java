package com.iikun.common.exception;

/**
 * author iikun
 * time 2026/2/5 0:53
 * version 1.0.0
 * msg:定义管理员异常
 */
public class NoAdminPermissionException extends RuntimeException {
    public NoAdminPermissionException() {
        super("没有管理员权限，禁止访问");
    }
}
