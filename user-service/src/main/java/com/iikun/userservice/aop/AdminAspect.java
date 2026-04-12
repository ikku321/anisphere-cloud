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
        LoginUser user = UserContext.getUser();
        log.info("获取用户权限: {}", user.getRole());
        if (!"0".equals(user.getRole())) {
            throw new ServiceException("没有管理员权限，禁止访问");
        }
    }

}
