package com.iikun.anivideo.handle;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.exception.NoAdminPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * author iikun
 * time 2025/9/15 1:30
 * version 1.0.0
 * msg: 全局异常处理
 */
@Slf4j
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

    @ExceptionHandler(DataAccessException.class)
    public Result<?> handleDatabaseException(DataAccessException e) {
        log.error("数据库异常", e);
        return Result.failed("数据库异常，请联系管理员");
    }
}