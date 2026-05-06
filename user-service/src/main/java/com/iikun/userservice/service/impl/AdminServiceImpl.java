package com.iikun.userservice.service.impl;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.JwtUtil;
import com.iikun.common.utils.PasswordUtil;
import com.iikun.common.utils.Utils;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.domain.model.AdminUserListItem;
import com.iikun.userservice.entity.User;
import com.iikun.userservice.mapper.AdminMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.AdminService;
import com.iikun.userservice.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author iikun
 * time 2025/9/21 0:29
 * version 1.0.0
 * msg: 用户管理操作接口实现类
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    /**
     * 允许登录后台管理端的角色集合：
     * <ul>
     *     <li>0 - 管理员：可访问所有管理端接口</li>
     *     <li>3 - 审核员：仅可访问审核相关接口（前端按 role 隐藏其它菜单）</li>
     * </ul>
     * 普通用户(1)与 UP 主(2)拒绝登录后台。
     */
    private static final Set<Integer> BACKEND_ALLOWED_ROLES = Set.of(0, 3);

    /** 管理端数据访问层 */
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

    @Override
    public String adminLogin(String username, String password) {
        try {
            // 查询用户信息
            User byUsername = userMapper.findByUsername(username);
            log.info("登录查询的用户信息: {}", byUsername);
            if (byUsername == null) {
                throw new ServiceException("用户不存在");
            }
            // 校验密码
            if (!PasswordUtil.matches(password, byUsername.getPassword())) {
                throw new ServiceException("密码不正确");
            }

            // 验证账号是否具备后台访问权限。
            // role 编码：0=管理员 1=普通用户 2=UP主 3=审核员（见 user.role 字段注释）。
            // 后台仅允许「管理员」与「审核员」登录；其余角色直接拒绝，避免拿到 token 后再被
            // 各 @Admin 接口连环 403 的体验。新增可登录角色时只需扩充该集合。
            Integer role = byUsername.getRole();
            if (role == null || !BACKEND_ALLOWED_ROLES.contains(role)) {
                throw new ServiceException("该账号无后台访问权限，请使用管理员或审核员账号登录");
            }

            // 生成 token
            // 常用的信息
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", String.valueOf(byUsername.getUserId()));
            claims.put("username", String.valueOf(byUsername.getUsername()));
            claims.put("role", String.valueOf(byUsername.getRole()));
            log.info("login-> calims存值: userId = {}", byUsername.getUserId());
            log.info("login-> calims存值: username = {}", byUsername.getUsername());
            log.info("login-> calims存值: role = {}", byUsername.getRole());

            String token = jwtUtil.generateToken(byUsername.getUserId(), claims);
            cacheToken(byUsername.getUserId(), token);
            return token;
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    private void cacheToken(String userId, String token) {
        String tokenFinal = normalizeToken(token);
        if (tokenFinal.isBlank()) {
            throw new ServiceException("Token生成失败");
        }

        Claims claims;
        try {
            claims = jwtUtil.getAllClaims(tokenFinal);
        } catch (Exception e) {
            throw new ServiceException("Token生成失败");
        }

        Date exp = claims.getExpiration();
        long ttlMs = exp == null ? 0 : exp.getTime() - System.currentTimeMillis();
        if (ttlMs <= 0) {
            ttlMs = 1000;
        }
        String key = "auth:token:" + sha256Hex(tokenFinal);
        try {
            stringRedisTemplate.opsForValue().set(key, userId, Duration.ofMillis(ttlMs));
        } catch (Exception e) {
            throw new ServiceException("登录失败: Token缓存异常");
        }
    }

    private static String normalizeToken(String raw) {
        if (raw == null) {
            return "";
        }
        String t = raw.trim();
        if (t.isEmpty()) {
            return "";
        }
        String lower = t.toLowerCase();
        if (lower.startsWith("bearer")) {
            t = t.substring(6).trim();
            if (t.startsWith(":")) {
                t = t.substring(1).trim();
            }
        }
        if ((t.startsWith("\"") && t.endsWith("\"")) || (t.startsWith("'") && t.endsWith("'"))) {
            t = t.substring(1, t.length() - 1).trim();
        }
        return t;
    }

    private static String sha256Hex(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return str;
        }
    }
}
