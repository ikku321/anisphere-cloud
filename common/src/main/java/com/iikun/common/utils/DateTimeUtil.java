package com.iikun.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * author iikun
 * time 2026/1/3 1:25
 * version 1.0.0
 * msg:时间工具类
 * 统一项目中的时间获取与格式化方式
 */
public class DateTimeUtil {

    /** 默认时间格式 */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 获取当前时间（LocalDateTime）
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    /**
     * 获取当前日期（LocalDate）
     */
    public static LocalDate today() {
        return LocalDate.now(ZoneId.systemDefault());
    }

    /**
     * 获取当前时间（字符串格式 yyyy-MM-dd HH:mm:ss）
     */
    public static String nowStr() {
        return format(now(), DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 获取当前日期字符串（yyyy-MM-dd）
     */
    public static String todayStr() {
        return format(today().atStartOfDay(), DEFAULT_DATE_FORMAT);
    }

    /**
     * 时间格式化
     */
    public static String format(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间戳（毫秒）
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
