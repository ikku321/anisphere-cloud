package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.request.BlackListRequestModel;
import com.iikun.userservice.domain.request.UserBlackListModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:24
 * version 1.0.0
 * msg: 用户黑名单Mapper
 */
@Mapper
public interface UserBlacklistMapper {

    /**
     * 加入黑名单用户
     *
     * @param uid         用户id
     * @param blacklistId 被拉黑对象用户id
     */
    @Insert("insert user_blacklist(user_id, blocked_user_id) values (#{uid}, #{blacklistId})")
    Integer joinBlack(@Param("uid") Integer uid, @Param("blacklistId") Integer blacklistId);

    /**
     * 查询拉黑表是否存在数据
     *
     * @param findUidById     操作者id
     * @param blackListUserId 操作对象id
     * @return 返回数量
     */
    @Select("select COUNT(*) from user_blacklist where user_id = #{findUidById} and blocked_user_id = #{blackListUserId}")
    Integer findIsExist(@Param("findUidById") Integer findUidById, @Param("blackListUserId") Integer blackListUserId);

    /**
     * 取消/移除黑名单
     *
     * @param findUidById     操作用户id
     * @param blackListUserId 操作目标id
     */
    @Delete("delete from user_blacklist where user_id = #{findUidById} and blocked_user_id = #{blackListUserId}")
    Integer cancelBlackList(@Param("findUidById") Integer findUidById, @Param("blackListUserId") Integer blackListUserId);

    /**
     * 获取黑名单列表（仅限管理员操作）
     *
     * @return 返回所有黑名单列表
     */
    List<BlackListRequestModel> findUserBlackListAll();

    /**
     * 查询指定账号的拉黑名单列表
     *
     * @param uid 当前用户账号uid
     * @return 返回拉黑列表
     */
    List<UserBlackListModel> findUserBlackList(@Param("uid") Integer uid);

    /**
     * 查询指定用户是否已经拉黑
     *
     * @param uid         当前用户uid
     * @param blacklistId 被查询用户uid
     * @return 返回状态: true： 已拉黑，false：没有被拉黑
     */
    @Select("select count(1) from user_blacklist where user_id = #{uid} and blocked_user_id = #{blacklistId}")
    Integer isBlackList(@Param("uid") String uid, @Param("blacklistId") String blacklistId);
}
