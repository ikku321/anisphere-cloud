package com.iikun.userservice.controller;

import com.iikun.common.annotation.Admin;
import com.iikun.common.annotation.OperationLog;
import com.iikun.common.annotation.RateLimit;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.enums.LimitType;
import com.iikun.common.utils.JwtUtil;
import com.iikun.common.utils.UserContext;
import com.iikun.userservice.domain.dto.RegisterDTO;
import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * author iikun
 * time 2025/9/17 14:26
 * version 1.0.0
 * msg: 用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "动漫视频社交系统用户基本管理")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserMapper userMapper;

    /**
     * 注册
     *
     * @param registerDTO 用户提交信息
     * @return 1
     */
    @PostMapping("/register")
    @Operation(summary = "注册账号", description = "注册")
    @RateLimit(key = "register", count = 5, period = 300, type = LimitType.IP, message = "注册过于频繁，请5分钟后再试")
    @OperationLog(module = "USER", operationType = "CREATE", businessType = "register", recordParams = true)
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }


    /**
     * 登录
     *
     * @param username 用户名/手机号
     * @param password 密码
     * @return token
     */
    @PostMapping("/login")
    @Operation(summary = "登录", description = "可以使用账号名称登录也可以使用手机号登录")
    @RateLimit(key = "login", count = 5, period = 60, type = LimitType.IP, message = "登录过于频繁，请1分钟后再试")
    @OperationLog(module = "USER", operationType = "LOGIN", businessType = "login", recordParams = false)
    public Result login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return Result.failed("账号或密码不能为空?");
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        String device = request.getHeader("User-Agent");
        return userService.login(username, password, ip, device);
    }


    /**
     * 查询用户基本信息
     *
     * @return 返回基本用户基本信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取用户基本信息")
    public Result info(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        return userService.info(uid);
    }


    /**
     * 修改当前用户邮箱信息
     * -- 后续添加邮箱验证码认证才能修改
     *
     * @param email 邮箱
     * @return 返回修改状态
     */
    @PostMapping("/update-email")
    @Operation(summary = "修改邮箱", description = "修改当前用户邮箱")
    @RateLimit(key = "update_email", count = 10, period = 60, type = LimitType.USER, message = "修改邮箱过于频繁")
    @OperationLog(module = "USER", operationType = "UPDATE", businessType = "update_email", recordParams = true)
    public Result updateEmail(@RequestParam String email) {
        if (email.isEmpty()) {
            return Result.failed("邮箱不能为空?");
        }
        // 从token中获取用户当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failed("未登录或 token 无效");
        }
        String userId = authentication.getPrincipal().toString();
        return userService.updataEmail(email, userId);
    }

    /**
     * 修改当前用户的手机号
     * -- 后续添加验证码认证
     *
     * @param newPhone 新手机号
     * @return 1
     */
    @PostMapping("/update-phone")
    @Operation(summary = "修改手机号", description = "修改当前用手机号，需要token")
    @RateLimit(key = "update_phone", count = 10, period = 60, type = LimitType.USER, message = "修改手机号过于频繁")
    @OperationLog(module = "USER", operationType = "UPDATE", businessType = "update_phone", recordParams = true)
    public Result updatePhone(@RequestParam String newPhone) {
        if (newPhone.isEmpty()) {
            return Result.failed("参数不能为空?");
        }
        // 从token中获取用户当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failed("未登录或 token 无效");
        }
        String userId = authentication.getPrincipal().toString();
        return userService.updatePhone(newPhone, userId);
    }


    /**
     * 修改昵称
     *
     * @param newNickname 新昵称
     * @return 1
     */
    @PostMapping("/update-nickname")
    @Operation(summary = "修改昵称", description = "修改当前用户昵称，需要token")
    @OperationLog(module = "USER", operationType = "UPDATE", businessType = "update_nickname", recordParams = true)
    public Result updateNickname(@RequestParam String newNickname) {
        if (newNickname.isEmpty()) {
            return Result.failed("参数不能为空?");
        }
        // 从token中获取用户当前用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failed("未登录或 token 无效");
        }
        String userId = authentication.getPrincipal().toString();
        return userService.updateNickName(newNickname, userId);
    }


    /**
     * 修改密码
     *
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 1
     */
    @PostMapping("/update-pwd")
    @Operation(summary = "修改密码", description = "修改当前用户的密码")
    @OperationLog(module = "USER", operationType = "UPDATE", businessType = "update_password", recordParams = false)
    public Result updatePwd(@RequestParam String oldPwd, @RequestParam String newPwd) {
        if (oldPwd.isEmpty() || newPwd.isEmpty()) {
            return Result.failed("参数不能为空?");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return Result.failed("未登录或 token 无效");
        }
        String userId = authentication.getPrincipal().toString();
        log.info("controller update pwd: {}", userId);
        return userService.updatePwd(oldPwd, newPwd, userId);
    }


    /**
     * 根据uid查询用户信息
     *
     * @param uid 用户uid
     * @return 1
     */
    @GetMapping("/find")
    @Operation(summary = "查询用户基本信息", description = "根据用户uid查询用户基本信息，密码等其他重要信息不展示")
    public Result<?> find(@RequestParam String uid) {
        if (uid.isEmpty()) {
            return Result.failed("参数不能为空?");
        }
        return Result.success(userService.findUidInfo(uid));
    }


    @Operation(summary = "根据token获取用户信息")
    @GetMapping("/found-token")
    public Result<UserInfoDTO> getTokenByUserInfo(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid").toString();
        return Result.success(userService.foundByTokenUserInfo(uid));
    }

    @Operation(summary = "更新用户头像地址")
    @PutMapping("/update-avatar-url")
    @OperationLog(module = "USER", operationType = "UPDATE", businessType = "update_avatar", recordParams = true)
    public Result<?> update(HttpServletRequest request, @RequestParam(required = true) String avatarUrl) {
        String uid = (String) request.getAttribute("uid").toString();
        userService.updateAvatarUrl(uid, avatarUrl);
        return Result.success();
    }


    @Admin
    @Operation(summary = "验证当前账号是否是管理员账号")
    @GetMapping("/is-admin")
    public Result<?> isAdmin() {
        return Result.success();
    }

    /**
     * 列出所有启用状态用户的 user_id 列表 (内部 Feign 调用专用).
     *
     * 用途: ani-message 模块在管理员发布公告时, 通过 Feign 调此接口拿到所有用户,
     *       然后批量插入 notification 表, 实现「公告 → 用户通知」的广播分发.
     *
     * 安全说明: 调用方 (AnnouncementAdminServiceImpl.publish) 已经做了 admin 校验,
     *         这里只返回 user_id (无敏感字段), 即使被普通账号调用也无法获得敏感信息.
     */
    @Operation(summary = "列出所有启用用户ID (内部接口)", description = "供 ani-message 公告广播分发用")
    @GetMapping("/internal/all-user-ids")
    public Result<java.util.List<String>> listAllActiveUserIds() {
        return Result.success(userMapper.listAllActiveUserIds());
    }
}




























