package com.iikun.userservice.aop;

import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import com.iikun.common.exception.NoAdminPermissionException;
import com.iikun.common.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * author iikun
 * time 2026/2/5 1:24
 * version 1.0.0
 * msg:AOP 判断 @Admin
 */
@Slf4j
@Aspect
@Component
public class AdminAspect {

    @Before("@annotation(com.iikun.common.annotation.Admin)")
    public void checkAdmin() {
        // 从当前上下文中获取登录用户信息
        LoginUser user = UserContext.getUser();
        
        // 校验用户是否已登录，避免 null 导致的空指针异常
        if (user == null) {
            log.warn("鉴权失败: 用户未登录或 Token 无效");
            throw new ServiceException("请先登录后操作");
        }
        
        log.info("校验用户权限: 用户ID = {}, 角色 = {}", user.getUid(), user.getRole());
        
        // 判断角色是否为管理员（约定 role 为 "0" 是管理员）
        if (!"0".equals(user.getRole())) {
            log.warn("权限不足: 用户ID = {}, 角色 = {}", user.getUid(), user.getRole());
            throw new ServiceException("没有管理员权限，禁止访问");
        }
    }

}
