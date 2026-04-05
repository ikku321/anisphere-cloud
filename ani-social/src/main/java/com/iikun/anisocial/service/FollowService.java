package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.Follow;
import com.iikun.common.base.Result;
import java.util.List;

/**
 * 关注/粉丝业务逻辑接口
 */
public interface FollowService extends IService<Follow> {

    /**
     * 关注用户
     *
     * @param userId     当前用户 ID
     * @param targetUser 被关注的目标用户 ID
     * @return 结果
     */
    Result<Void> follow(String userId, String targetUser);

    /**
     * 取消关注
     *
     * @param userId     当前用户 ID
     * @param targetUser 被取消关注的目标用户 ID
     * @return 结果
     */
    Result<Void> unfollow(String userId, String targetUser);

    /**
     * 获取关注列表
     *
     * @param userId 指定用户的 ID
     * @return 关注列表
     */
    Result<List<Follow>> getFollowingList(String userId);

    /**
     * 获取粉丝列表
     *
     * @param userId 指定用户的 ID
     * @return 粉丝列表
     */
    Result<List<Follow>> getFollowersList(String userId);
}
