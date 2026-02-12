package com.iikun.userservice.service;

/**
 * author iikun
 * time 2025/9/19 23:13
 * version 1.0.0
 * msg: 用户收藏表接口定义层
 */
public interface UserCollectionService {

    /**
     * 新增一条收藏记录。
     * <p>
     * 说明：
     * - uid 为业务用户ID（user.user_id）。
     * - user_collection.user_id 外键指向 user.id（自增主键）。
     * - Service 负责转换 uid -> user.id。
     * </p>
     *
     * @param uid        业务用户ID（user.user_id）
     * @param targetType 收藏类型(0视频 1漫画 2评论)
     * @param targetId   收藏目标ID
     */
    void add(String uid, Integer targetType, Long targetId);

    /**
     * 取消收藏。
     *
     * @param uid        业务用户ID（user.user_id）
     * @param targetType 收藏类型(0视频 1漫画 2评论)
     * @param targetId   收藏目标ID
     */
    void cancel(String uid, Integer targetType, Long targetId);

    /**
     * 分页查询当前用户的收藏列表。
     *
     * @param uid  业务用户ID（user.user_id）
     * @param page 页码，从 1 开始
     * @param size 每页大小
     * @return 分页数据（含 total、list 等信息）
     */
    Object pageMyCollections(String uid, Integer page, Integer size);

    /**
     * 查询是否已收藏。
     *
     * @param uid        业务用户ID（user.user_id）
     * @param targetType 收藏类型
     * @param targetId   收藏目标ID
     * @return true=已收藏，false=未收藏
     */
    boolean isCollected(String uid, Integer targetType, Long targetId);

}
