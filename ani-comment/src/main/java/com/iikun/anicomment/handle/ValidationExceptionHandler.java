package com.iikun.anicomment.handle;

import com.iikun.common.base.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 参数校验异常处理：
 *
 * 说明：
 * - common 模块的 GlobalExceptionHandler 目前主要处理 ServiceException 等。
 * - 这里补齐对 @Valid 校验失败的统一返回格式，避免前端拿到默认的错误结构。
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * 处理 @RequestBody + @Valid 的校验异常。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().isEmpty()
                ? "参数校验失败"
                : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.validateFailed(msg);
    }

    /**
     * 处理表单/路径参数等触发的校验异常。
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = e.getBindingResult().getAllErrors().isEmpty()
                ? "参数校验失败"
                : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.validateFailed(msg);
    }
}
