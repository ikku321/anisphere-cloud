package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.entity.UserCollection;
import com.iikun.userservice.mapper.UserCollectionMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserCollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2025/9/19 23:14
 * version 1.0.0
 * msg: 用户收藏表操作接口实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserCollectionServiceImpl implements UserCollectionService {

    /** 用户收藏数据访问层 */
    private final UserCollectionMapper userCollectionMapper;

    /** 用户数据访问层：用于 uid -> user.id 转换 */
    private final UserMapper userMapper;

    @Override
    public void add(String uid, Integer targetType, Long targetId) {
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

        // 先查一遍，避免直接依赖唯一键异常（也便于返回更友好的提示）
        if (userCollectionMapper.exists(userPkId.longValue(), targetType, targetId) > 0) {
            throw new ServiceException("已收藏，无需重复收藏");
        }

        try {
            int inserted = userCollectionMapper.insert(userPkId.longValue(), targetType, targetId);
            if (inserted <= 0) {
                throw new ServiceException("收藏失败");
            }
        } catch (DuplicateKeyException e) {
            // 并发情况下可能同时插入，唯一键触发，此时也视为已收藏
            throw new ServiceException("已收藏，无需重复收藏");
        }
    }

    @Override
    public void cancel(String uid, Integer targetType, Long targetId) {
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

        int deleted = userCollectionMapper.delete(userPkId.longValue(), targetType, targetId);
        if (deleted <= 0) {
            throw new ServiceException("取消收藏失败（可能未收藏该内容）");
        }
    }

    @Override
    public Object pageMyCollections(String uid, Integer page, Integer size) {
        if (uid == null || uid.isEmpty()) {
            throw new ServiceException("uid不能为空");
        }
        int pageNo = page == null || page < 1 ? 1 : page;
        int pageSize = size == null || size < 1 ? 10 : Math.min(size, 100);

        Integer userPkId = userMapper.getFindUidById(uid);
        if (userPkId == null) {
            throw new ServiceException("用户不存在");
        }

        long total = userCollectionMapper.countByUserId(userPkId.longValue());
        int offset = (pageNo - 1) * pageSize;
        List<UserCollection> list = userCollectionMapper.selectPageByUserId(userPkId.longValue(), offset, pageSize);

        Map<String, Object> result = new HashMap<>();
        result.put("page", pageNo);
        result.put("size", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public boolean isCollected(String uid, Integer targetType, Long targetId) {
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

        return userCollectionMapper.exists(userPkId.longValue(), targetType, targetId) > 0;
    }

}
