package com.iikun.userservice.mapper;

import com.iikun.userservice.entity.UserCollection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:14
 * version 1.0.0
 * msg: 用户收藏表Mapper
 */
@Mapper
public interface UserCollectionMapper {

    /**
     * 新增收藏。
     *
     * @param userId     user.id 自增主键
     * @param targetType 收藏类型
     * @param targetId   收藏目标ID
     * @return 影响行数
     */
    int insert(@Param("userId") Long userId,
               @Param("targetType") Integer targetType,
               @Param("targetId") Long targetId);

    /**
     * 取消收藏。
     *
     * @param userId     user.id 自增主键
     * @param targetType 收藏类型
     * @param targetId   收藏目标ID
     * @return 影响行数
     */
    int delete(@Param("userId") Long userId,
               @Param("targetType") Integer targetType,
               @Param("targetId") Long targetId);

    /**
     * 判断是否已收藏。
     */
    int exists(@Param("userId") Long userId,
               @Param("targetType") Integer targetType,
               @Param("targetId") Long targetId);

    /**
     * 统计当前用户收藏总数。
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 分页查询收藏。
     */
    List<UserCollection> selectPageByUserId(@Param("userId") Long userId,
                                            @Param("offset") Integer offset,
                                            @Param("size") Integer size);
}
