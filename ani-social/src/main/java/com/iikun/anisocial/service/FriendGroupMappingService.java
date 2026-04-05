package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.FriendGroupMapping;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 好友-分组映射业务逻辑接口
 */
public interface FriendGroupMappingService extends IService<FriendGroupMapping> {

    /**
     * 将好友添加到分组
     *
     * @param userId   当前用户 ID
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    Result<Void> addFriendToGroup(String userId, Long groupId, String friendId);

    /**
     * 将好友从分组移除
     *
     * @param userId   当前用户 ID
     * @param groupId  分组 ID
     * @param friendId 好友 ID
     * @return 结果
     */
    Result<Void> removeFriendFromGroup(String userId, Long groupId, String friendId);

    /**
     * 获取指定分组下的所有好友映射记录
     *
     * @param groupId 分组 ID
     * @return 映射记录列表
     */
    Result<List<FriendGroupMapping>> getGroupFriends(Long groupId);
}
