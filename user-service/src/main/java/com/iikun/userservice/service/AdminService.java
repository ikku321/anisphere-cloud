package com.iikun.userservice.service;

import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.entity.User;

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

    Object pageUsers(Integer page, Integer size, String keyword, Integer status, Integer role);

    UserInfoDTO getUserInfo(String userId);

    void updateUserStatus(String userId, Integer status);

    void updateUserRole(String userId, Integer role);

    void resetPassword(String userId, String newPassword);

    String createUser(String username, String password, String nickname, String phone, String email, Integer role, Integer status);

    void updateUser(String userId, User update);
}
