package com.iikun.userservice.service;

import com.iikun.common.base.Result;
import com.iikun.userservice.domain.request.BlackListRequestModel;
import com.iikun.userservice.domain.request.UserBlackListModel;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:23
 * version 1.0.0
 * msg: 用户黑名单操作接口定义
 */
public interface UserBlacklistService {

    /**
     * 加入黑名单用户
     *
     * @param uid         用户id
     * @param blacklistId 被拉黑对象用户id
     */
    void jionBlack(String uid, String blacklistId);

    /**
     * 取消/移除黑名单
     *
     * @param uid         操作用户id
     * @param blacklistId 操作目标id
     */
    void cancelBlackList(String uid, String blacklistId);

    /**
     * 查询所有黑名单人员信息（仅限管理员操作）
     */
    List<BlackListRequestModel> all();

    /**
     * 查询当前账号下的黑名单列表
     * @param uid 当前账号uid
     */
    List<UserBlackListModel> findBlackList(String uid);

    /**
     * 查询该用户是否已经被拉黑
     *
     * @param uid         当前用户id
     * @param blacklistId 被查询用户id
     */
    String findIsBlackList(String uid, String blacklistId);
}











