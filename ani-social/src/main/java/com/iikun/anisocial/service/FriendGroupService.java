package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.FriendGroup;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 好友分组业务逻辑接口
 */
public interface FriendGroupService extends IService<FriendGroup> {

    /**
     * 创建好友分组
     *
     * @param userId    当前用户 ID
     * @param groupName 分组名称
     * @return 结果
     */
    Result<FriendGroup> createGroup(String userId, String groupName);

    /**
     * 删除好友分组
     *
     * @param userId  当前用户 ID
     * @param groupId 分组 ID
     * @return 结果
     */
    Result<Void> deleteGroup(String userId, Long groupId);

    /**
     * 获取用户的所有分组
     *
     * @param userId 当前用户 ID
     * @return 分组列表
     */
    Result<List<FriendGroup>> getUserGroups(String userId);

    /**
     * 修改分组名称
     *
     * @param userId  当前用户 ID
     * @param groupId 分组 ID
     * @param name    新名称
     * @return 结果
     */
    Result<Void> renameGroup(String userId, Long groupId, String name);
}
