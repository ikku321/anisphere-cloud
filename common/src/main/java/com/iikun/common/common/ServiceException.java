package com.iikun.common.common;

/**
 * author iikun
 * time 2025/10/17 16:26
 * version 1.0.0
 * msg:
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}