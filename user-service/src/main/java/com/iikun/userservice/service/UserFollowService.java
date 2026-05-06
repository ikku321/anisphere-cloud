package com.iikun.userservice.service;

import com.iikun.common.base.Result;
import com.iikun.userservice.domain.model.AttentionListModel;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:30
 * version 1.0.0
 * msg: 用户关注/粉丝操作接口定义层
 */
public interface UserFollowService {

    /**
     * 关注
     *
     * @param uid      操作者id
     * @param followId 被关注人id
     */
    void attention(String uid, String followId);

    /**
     * 取消关注
     *
     * @param uid      操作者uid
     * @param followId 被取消用户id
     */
    void cancelAttention(String uid, String followId);


    /**
     * 获取关注列表（uid 关注了谁）。
     *
     * @param uid      列表所属者 uid
     * @param viewerId 当前查看者 uid，用于计算每行的 isMyFollowing；可为空
     */
    List<AttentionListModel> selectAllFollow(String uid, String viewerId);

    /**
     * 获取粉丝列表（谁关注了 uid）。
     *
     * @param uid      列表所属者 uid（即被关注者）
     * @param viewerId 当前查看者 uid，用于计算每行的 isMyFollowing；可为空
     */
    List<AttentionListModel> selectAllFans(String uid, String viewerId);

    /**
     * 判断当前用户是否已关注某人。
     *
     * @param uid      操作者 uid
     * @param targetId 目标用户 uid
     * @return true 已关注；false 未关注
     */
    boolean isFollowing(String uid, String targetId);
}
