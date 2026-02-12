package com.iikun.userservice.service.impl;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.model.AttentionListModel;
import com.iikun.userservice.mapper.UserFollowMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserFollowService;
import com.iikun.userservice.service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void attention(String uid, String followId) {
        // 判断该用户id是否存在
        if (userMapper.selectByUserId(uid) < 0) {
            throw new ServiceException("当前用户不存在? 请重新登录!");
        }

        // 查询是否已经关注（防止网络影响的情况下没加载完整显示未关注导致点击关注，需要查询）
        if (userFollowMapper.selectByFollowing(followId) > 0) {
            throw new ServiceException("您已关注该用户");
        }

        // 执行关注操作
        Integer add = userFollowMapper.add(uid, followId);
        log.info("关注状态: {}", add);
        if (add == null || add <= 0) throw new ServiceException("关注失败!");
    }

    /**
     * 取消关注
     *
     * @param uid      操作者uid
     * @param followId 被取消用户id
     */
    @Override
    public void cancelAttention(String uid, String followId) {
        // 判断是否为空
        if (followId.isEmpty()) {
            throw new ServiceException("被关注用户id参数为空!");
        }
        // 以防万一，加个判断
        if (userMapper.selectByUserId(uid) < 0) {
            throw new ServiceException("当前用户不存在? 请重新登录!");
        }
        if (userFollowMapper.selectByFollowing(followId) <= 0) {
            throw new ServiceException("您已取消关注该用户");
        }
        // 执行删除/取消关注
        Integer deleted = userFollowMapper.delete(uid, followId);
        if (deleted < 0) {
            throw new ServiceException("取消关注失败!");
        }
    }

    /**
     * 获取关注列表
     *
     * @param uid 操作者id
     */
    @Override
    public List<AttentionListModel> selectAllFollow(String uid) {
        List<AttentionListModel> attentionListModels = userFollowMapper.selectAttentionList(uid);
        if (attentionListModels == null || attentionListModels.isEmpty()) {
            throw new ServiceException("查询失败？");
        }
        return attentionListModels;
    }
}


















