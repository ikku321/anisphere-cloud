package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.model.AttentionListModel;
import com.iikun.userservice.mapper.UserFollowMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserFollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:30
 * version 1.0.0
 * msg: 用户关注/粉丝操作接口定义实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserFollowServiceImpl implements UserFollowService {

    // 用户关注/粉丝数据操作 Mapper
    private final UserFollowMapper userFollowMapper;

    // 用户数据操作 Mapper
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void attention(String uid, String followId) {
        if (uid == null || uid.isBlank()) {
            throw new ServiceException("当前用户未登录");
        }
        if (followId == null || followId.isBlank()) {
            throw new ServiceException("被关注用户id参数为空!");
        }
        if (uid.equals(followId)) {
            throw new ServiceException("不能关注自己");
        }

        // 操作者必须存在
        if (userMapper.selectByUserId(uid) <= 0) {
            throw new ServiceException("当前用户不存在? 请重新登录!");
        }
        // 目标用户也必须存在（避免脏数据）
        if (userMapper.selectByUserId(followId) <= 0) {
            throw new ServiceException("目标用户不存在");
        }

        // 是否已关注（按真实 follower+following 组合判定）
        Integer existed = userFollowMapper.existsByPair(uid, followId);
        if (existed != null && existed > 0) {
            throw new ServiceException("您已关注该用户");
        }

        // 写关系表
        Integer add = userFollowMapper.add(uid, followId);
        if (add == null || add <= 0) {
            throw new ServiceException("关注失败!");
        }

        // 同步物化字段
        userFollowMapper.incFollowingCount(uid);
        userFollowMapper.incFollowersCount(followId);
        log.info("[follow] {} 关注 {} 成功", uid, followId);
    }

    /**
     * 取消关注
     *
     * @param uid      操作者uid
     * @param followId 被取消用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAttention(String uid, String followId) {
        if (uid == null || uid.isBlank()) {
            throw new ServiceException("当前用户未登录");
        }
        if (followId == null || followId.isBlank()) {
            throw new ServiceException("被关注用户id参数为空!");
        }
        if (userMapper.selectByUserId(uid) <= 0) {
            throw new ServiceException("当前用户不存在? 请重新登录!");
        }

        // 当前确实在关注关系中才能取关（按 follower+following 组合）
        Integer existed = userFollowMapper.existsByPair(uid, followId);
        if (existed == null || existed <= 0) {
            throw new ServiceException("您未关注该用户");
        }

        // 执行删除
        Integer deleted = userFollowMapper.delete(uid, followId);
        if (deleted == null || deleted <= 0) {
            throw new ServiceException("取消关注失败!");
        }

        // 同步物化字段（不低于 0）
        userFollowMapper.decFollowingCount(uid);
        userFollowMapper.decFollowersCount(followId);
        log.info("[follow] {} 取关 {} 成功", uid, followId);
    }

    /**
     * 获取关注列表（uid 关注了谁）。
     * SQL 已经在 LEFT JOIN 里算好了 isMyFollowing。
     */
    @Override
    public List<AttentionListModel> selectAllFollow(String uid, String viewerId) {
        if (uid == null || uid.isBlank()) {
            return Collections.emptyList();
        }
        List<AttentionListModel> list = userFollowMapper.selectAttentionList(uid, viewerId);
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 获取粉丝列表（谁关注了 uid）。
     */
    @Override
    public List<AttentionListModel> selectAllFans(String uid, String viewerId) {
        if (uid == null || uid.isBlank()) {
            return Collections.emptyList();
        }
        List<AttentionListModel> list = userFollowMapper.selectFansList(uid, viewerId);
        return list == null ? Collections.emptyList() : list;
    }

    /**
     * 判断当前用户是否已关注某人。
     * 未登录或参数缺失一律返回 false（不抛异常，便于 UI 默认渲染「关注」按钮）。
     */
    @Override
    public boolean isFollowing(String uid, String targetId) {
        if (uid == null || uid.isBlank() || targetId == null || targetId.isBlank()) {
            return false;
        }
        if (uid.equals(targetId)) {
            return false;
        }
        Integer existed = userFollowMapper.existsByPair(uid, targetId);
        return existed != null && existed > 0;
    }
}


















