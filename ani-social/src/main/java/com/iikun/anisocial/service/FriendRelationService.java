package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.FriendRelation;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 好友关系业务逻辑接口
 */
public interface FriendRelationService extends IService<FriendRelation> {

    /**
     * 发送好友申请
     *
     * @param userId   当前用户 ID
     * @param friendId 目标好友 ID
     * @return 结果
     */
    Result<Void> sendFriendRequest(String userId, String friendId);

    /**
     * 处理好友申请（接受、拒绝、拉黑）
     *
     * @param userId   当前用户 ID
     * @param friendId 申请人 ID
     * @param status   状态：1=接受 2=拒绝/拉黑
     * @return 结果
     */
    Result<Void> processFriendRequest(String userId, String friendId, Integer status);

    /**
     * 获取好友列表
     *
     * @param userId 当前用户 ID
     * @return 好友列表
     */
    Result<List<FriendRelation>> getFriendList(String userId);

    /**
     * 修改好友备注
     *
     * @param userId   当前用户 ID
     * @param friendId 好友 ID
     * @param remark   备注名
     * @return 结果
     */
    Result<Void> updateRemark(String userId, String friendId, String remark);

    /**
     * 删除好友
     *
     * @param userId   当前用户 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    /**
     * 删除好友
     *
     * @param userId   当前用户 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    Result<Void> deleteFriend(String userId, String friendId);

    /**
     * 获取共同好友列表
     *
     * @param userId       当前用户 ID
     * @param otherUserId  另一个用户 ID
     * @return 共同好友 ID 列表
     */
    Result<List<String>> getMutualFriends(String userId, String otherUserId);

    /**
     * 拉黑用户
     *
     * @param userId     当前用户 ID
     * @param targetUser 目标用户 ID
     * @return 结果
     */
    Result<Void> blockUser(String userId, String targetUser);

    /**
     * 取消拉黑
     *
     * @param userId     当前用户 ID
     * @param targetUser 目标用户 ID
     * @return 结果
     */
    Result<Void> unblockUser(String userId, String targetUser);
}
