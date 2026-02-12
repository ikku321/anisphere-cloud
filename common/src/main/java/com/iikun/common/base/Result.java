package com.iikun.common.base;

/**
 * author iikun
 * time 2025/9/15 1:18
 * version 1.0.0
 * msg:
 */

import com.iikun.common.Enum.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果封装
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 什么都没有
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), null);
    }

    // 成功
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.code(), message, data);
    }

    // 失败
    public static <T> Result<T> failed(String message) {
        return new Result<>(ResultCode.FAILED.code(), message, null);
    }

    public static <T> Result<T> failed() {
        return new Result<>(ResultCode.FAILED.code(), ResultCode.FAILED.message(), null);
    }

    // 参数校验失败
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ResultCode.VALIDATE_FAILED.code(), message, null);
    }

    // 未登录
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(ResultCode.UNAUTHORIZED.code(), message, null);
    }

    // 无权限
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(ResultCode.FORBIDDEN.code(), message, null);
    }
}