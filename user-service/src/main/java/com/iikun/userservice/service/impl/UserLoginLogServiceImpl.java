package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.entity.UserLoginLog;
import com.iikun.userservice.mapper.UserLoginLogMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /** 用户登录日志数据访问层 */
    private final UserLoginLogMapper userLoginLogMapper;

    /** 用户数据访问层，用于 uid -> user.id 转换 */
    private final UserMapper userMapper;

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
