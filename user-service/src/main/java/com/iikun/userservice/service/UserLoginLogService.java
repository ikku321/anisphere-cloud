package com.iikun.userservice.service;

/**
 * author iikun
 * time 2025/9/19 23:18
 * version 1.0.0
 * msg: 用户登录日志表接口定义
 */
public interface UserLoginLogService {

    /**
     * 写入一条登录日志。
     * <p>
     * 说明：
     * - Controller 侧从请求中拿到的 uid 为业务用户ID（user.user_id）。
     * - 数据库表 user_login_log.user_id 外键指向 user.id（自增主键）。
     * - 因此 Service 层负责做一次 uid -> user.id 的转换再落库。
     * </p>
     *
     * @param uid    业务用户ID（user.user_id）
     * @param ip     登录IP（可为空）
     * @param device 登录设备信息（可为空）
     * @param status 登录状态：0失败 / 1成功
     */
    void record(String uid, String ip, String device, Integer status);

    /**
     * 查询当前用户自己的登录日志。
     *
     * @param uid  业务用户ID（user.user_id）
     * @param page 页码，从 1 开始
     * @param size 每页大小
     * @return 登录日志列表
     */
    Object pageMyLogs(String uid, Integer page, Integer size);
}
