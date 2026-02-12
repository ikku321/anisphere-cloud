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
     * 获取关注列表
     *
     * @param uid 操作者id
     */
    List<AttentionListModel> selectAllFollow(String uid);
}
