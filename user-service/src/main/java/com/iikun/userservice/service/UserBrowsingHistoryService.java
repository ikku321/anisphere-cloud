package com.iikun.userservice.service;

/**
 * author iikun
 * time 2025/9/19 23:09
 * version 1.0.0
 * msg: 用户浏览记录接口定义表
 */
public interface UserBrowsingHistoryService {

    /**
     * 记录一次浏览行为。
     * <p>
     * 说明：
     * - uid 为业务用户ID（user.user_id）。
     * - user_browsing_history.user_id 外键指向 user.id（自增主键）。
     * - Service 负责转换 uid -> user.id。
     * </p>
     *
     * @param uid        业务用户ID（user.user_id）
     * @param targetType 内容类型(0视频 1漫画 2评论 3其他)
     * @param targetId   目标内容ID
     */
    void record(String uid, Integer targetType, Long targetId);

    /**
     * 删除一条浏览记录。
     *
     * @param uid      业务用户ID（user.user_id）
     * @param recordId 浏览记录自增ID
     */
    void delete(String uid, Long recordId);

    /**
     * 分页查询当前用户的浏览记录。
     *
     * @param uid  业务用户ID（user.user_id）
     * @param page 页码，从 1 开始
     * @param size 每页大小
     * @return 分页数据（含 total、list 等信息）
     */
    Object pageMyHistory(String uid, Integer page, Integer size);

}
