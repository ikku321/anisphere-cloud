package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.PasswordUtil;
import com.iikun.common.utils.Utils;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.domain.model.AdminUserListItem;
import com.iikun.userservice.entity.User;
import com.iikun.userservice.mapper.AdminMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/21 0:29
 * version 1.0.0
 * msg: 用户管理操作接口实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    /** 管理端数据访问层 */
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Override
    public Object pageUsers(Integer page, Integer size) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);

        long total = adminMapper.countUsers();
        int offset = (pageNo - 1) * pageSize;

        List<AdminUserListItem> list = adminMapper.selectUserPage(offset, pageSize);
        if (list == null) {
            throw new ServiceException("查询用户列表失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("page", pageNo);
        result.put("size", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public Object pageUsers(Integer page, Integer size, String keyword, Integer status, Integer role) {
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);

        long total = adminMapper.countUsersByFilter(keyword, status, role);
        int offset = (pageNo - 1) * pageSize;

        List<AdminUserListItem> list = adminMapper.selectUserPageByFilter(offset, pageSize, keyword, status, role);
        if (list == null) {
            throw new ServiceException("查询用户列表失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("page", pageNo);
        result.put("size", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public UserInfoDTO getUserInfo(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        UserInfoDTO dto = userMapper.foundUserInfo(userId);
        if (dto == null) {
            throw new ServiceException("用户不存在");
        }
        return dto;
    }

    @Override
    public void updateUserStatus(String userId, Integer status) {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (status == null || status < 0 || status > 3) {
            throw new ServiceException("status参数不合法");
        }
        Integer exists = userMapper.selectByUserId(userId);
        if (exists == null || exists <= 0) {
            throw new ServiceException("用户不存在");
        }
        if (adminMapper.updateUserStatus(userId, status) <= 0) {
            throw new ServiceException("更新用户状态失败");
        }
    }

    @Override
    public void updateUserRole(String userId, Integer role) {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (role == null || role < 0 || role > 3) {
            throw new ServiceException("role参数不合法");
        }
        Integer exists = userMapper.selectByUserId(userId);
        if (exists == null || exists <= 0) {
            throw new ServiceException("用户不存在");
        }
        if (adminMapper.updateUserRole(userId, role) <= 0) {
            throw new ServiceException("更新用户角色失败");
        }
    }

    @Override
    public void resetPassword(String userId, String newPassword) {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ServiceException("newPassword不能为空");
        }
        Integer exists = userMapper.selectByUserId(userId);
        if (exists == null || exists <= 0) {
            throw new ServiceException("用户不存在");
        }
        String encoded = PasswordUtil.encode(newPassword);
        if (adminMapper.updateUserPassword(userId, encoded) <= 0) {
            throw new ServiceException("重置密码失败");
        }
    }

    @Override
    public String createUser(String username, String password, String nickname, String phone, String email, Integer role, Integer status) {
        if (username == null || username.isEmpty()) {
            throw new ServiceException("username不能为空");
        }
        if (password == null || password.isEmpty()) {
            throw new ServiceException("password不能为空");
        }

        String userId = Utils.shortUUID();
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(PasswordUtil.encode(password));
        user.setNickname((nickname == null || nickname.isEmpty()) ? ("新用户" + Utils.shortUUID()) : nickname);
        user.setPhone(phone);
        user.setEmail(email);

        try {
            int inserted = userMapper.register(user);
            if (inserted <= 0) {
                throw new ServiceException("创建用户失败");
            }
        } catch (DuplicateKeyException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("user.email") || msg.contains("uk_user_email")) {
                throw new ServiceException("该邮箱已经绑定其他账号");
            }
            if (msg.contains("user.phone") || msg.contains("uk_user_phone")) {
                throw new ServiceException("该手机号已经绑定其他账号");
            }
            if (msg.contains("user.uk_user_name") || msg.contains("uk_user_name")) {
                throw new ServiceException("该用户名已存在");
            }
            if (msg.contains("uk_user_id")) {
                throw new ServiceException("用户ID冲突，请重试");
            }
            throw new ServiceException("创建用户失败");
        }

        if (role != null) {
            updateUserRole(userId, role);
        }
        if (status != null) {
            updateUserStatus(userId, status);
        }

        return userId;
    }

    @Override
    public void updateUser(String userId, User update) {
        if (userId == null || userId.isEmpty()) {
            throw new ServiceException("userId不能为空");
        }
        if (update == null) {
            throw new ServiceException("更新内容不能为空");
        }
        Integer exists = userMapper.selectByUserId(userId);
        if (exists == null || exists <= 0) {
            throw new ServiceException("用户不存在");
        }

        update.setUserId(userId);
        update.setPassword(null);
        update.setRole(null);
        update.setStatus(null);

        if (adminMapper.updateUser(update) <= 0) {
            throw new ServiceException("更新用户信息失败");
        }
    }
}
