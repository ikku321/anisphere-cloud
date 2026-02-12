package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.entity.UserBrowsingHistory;
import com.iikun.userservice.mapper.UserBrowsingHistoryMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserBrowsingHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/19 23:09
 * version 1.0.0
 * msg: 用户浏览记录操作接口实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserBrowsingHistoryServiceImpl implements UserBrowsingHistoryService {

    /** 浏览记录数据访问层 */
    private final UserBrowsingHistoryMapper userBrowsingHistoryMapper;

    /** 用户数据访问层，用于 uid -> user.id 转换 */
    private final UserMapper userMapper;

    @Override
    public void record(String uid, Integer targetType, Long targetId) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        if (targetType == null || targetType < 0) {
            throw new ServiceException("targetType参数不合法");
        }
        if (targetId == null || targetId <= 0) {
            throw new ServiceException("targetId参数不合法");
        }

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在");
        }

        int inserted = userBrowsingHistoryMapper.insert(userPkId.longValue(), targetType, targetId);
        if (inserted <= 0) {
            throw new ServiceException("写入浏览记录失败");
        }
    }

    @Override
    public void delete(String uid, Long recordId) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        if (recordId == null || recordId <= 0) {
            throw new ServiceException("recordId参数不合法");
        }

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在");
        }

        int deleted = userBrowsingHistoryMapper.deleteById(userPkId.longValue(), recordId);
        if (deleted <= 0) {
            throw new ServiceException("删除失败（记录不存在或无权限）");
        }
    }

    @Override
    public Object pageMyHistory(String uid, Integer page, Integer size) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在");
        }

        long total = userBrowsingHistoryMapper.countByUserId(userPkId.longValue());
        int offset = (pageNo - 1) * pageSize;
        List<UserBrowsingHistory> list = userBrowsingHistoryMapper.selectPageByUserId(userPkId.longValue(), offset, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("page", pageNo);
        result.put("size", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }
}
