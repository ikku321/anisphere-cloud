package com.iikun.common.annotation;

import java.lang.annotation.*;

/**
 * author iikun
 * time 2026/2/5 0:51
 * version 1.0.0
 * msg: 定义管理员注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Admin {
}
