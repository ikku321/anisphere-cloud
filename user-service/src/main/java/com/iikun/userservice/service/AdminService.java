package com.iikun.userservice.service;

/**
 * author iikun
 * time 2025/9/21 0:28
 * version 1.0.0
 * msg: 用户管理操作接口定义层
 */
public interface AdminService {

    /**
     * 分页查询用户列表（管理端）。
     * <p>
     * 注意：
     * - 该接口仅用于管理端展示，不返回 password 等敏感字段。
     * - 返回结构为通用分页Map：page/size/total/list。
     * </p>
     *
     * @param page 页码，从 1 开始
     * @param size 每页大小
     * @return 分页数据
     */
    Object pageUsers(Integer page, Integer size);
}
