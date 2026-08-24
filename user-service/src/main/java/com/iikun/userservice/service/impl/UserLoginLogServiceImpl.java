package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.common.entity.SysLoginLog;
import com.iikun.common.utils.IpUtils;
import com.iikun.common.utils.TraceIdUtils;
import com.iikun.userservice.entity.UserLoginLog;
import com.iikun.userservice.mapper.UserLoginLogMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.SysLoginLogService;
import com.iikun.userservice.service.UserLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/19 23:19
 * version 1.0.0
 * msg: 用户登录日志操作接口实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserLoginLogServiceImpl implements UserLoginLogService {

    private final UserLoginLogMapper userLoginLogMapper;
    private final UserMapper userMapper;
    private final SysLoginLogService sysLoginLogService;

    @Override
    public void record(String uid, String ip, String device, Integer status) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        if (status == null || (status != 0 && status != 1)) {
            throw new ServiceException("status参数不合法");
        }

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在，请重新登录");
        }

        int inserted = userLoginLogMapper.insert(userPkId.longValue(), ip, device, status);
        if (inserted <= 0) {
            throw new ServiceException("写入登录日志失败");
        }

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

            String username = null;
            if (request != null) {
                username = request.getHeader("X-Username");
            }

            SysLoginLog sysLog = SysLoginLog.builder()
                    .traceId(TraceIdUtils.get())
                    .userId(uid)
                    .username(username)
                    .loginType("PASSWORD")
                    .ipAddress(ip)
                    .browser(device)
                    .status(status)
                    .message(status == 1 ? "登录成功" : "登录失败")
                    .createTime(LocalDateTime.now())
                    .build();

            sysLoginLogService.saveLogAsync(sysLog);
        } catch (Exception e) {
            log.warn("写入sys_login_log失败: {}", e.getMessage());
        }
    }

    @Override
    public Object pageMyLogs(String uid, Integer page, Integer size) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在");
        }

        long total = userLoginLogMapper.countByUserId(userPkId.longValue());
        int offset = (pageNo - 1) * pageSize;
        List<UserLoginLog> list = userLoginLogMapper.selectPageByUserId(userPkId.longValue(), offset, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("page", pageNo);
        result.put("size", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }
}
