package com.iikun.common.handler;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.exception.NoAdminPermissionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * author iikun
 * time 2025/9/15 1:30
 * version 1.0.0
 * msg: 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常 ServiceException
     *
     * @param e ServiceException 异常对象
     * @return Result 封装的统一错误响应
     */
    @ExceptionHandler(ServiceException.class)
    public Result<?> handleServiceException(ServiceException e) {
        // 返回统一格式的错误信息
        return Result.failed(e.getMessage());
    }

    /**
     * 处理无管理员权限异常。
     *
     * @param e NoAdminPermissionException
     * @return 统一响应（403）
     */
    @ExceptionHandler(NoAdminPermissionException.class)
    public Result<?> handleNoAdminPermissionException(NoAdminPermissionException e) {
        return Result.forbidden(e.getMessage());
    }

    // 处理通用异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.failed("服务器内部错误: " + e.getMessage());
    }
}
