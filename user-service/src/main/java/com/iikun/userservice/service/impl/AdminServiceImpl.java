package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.model.AdminUserListItem;
import com.iikun.userservice.mapper.AdminMapper;
import com.iikun.userservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
