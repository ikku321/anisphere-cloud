package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.model.AttentionListModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:31
 * version 1.0.0
 * msg: 用户 关注/粉丝Mapper
 */
@Mapper
public interface UserFollowMapper {

    /**
     * 关注up主/创作者/其他用户
     *
     * @param follower  操作者id
     * @param following 被关注者id
     * @return 返回状态
     */
    @Insert("insert into user_follow(follower_id, following_id) " +
            "VALUES (#{follower}, #{following})")
    Integer add(@Param("follower") String follower, @Param("following") String following);


    /**
     * 取消关注
     * 删除关注表中得数据
     *
     * @param follower  操作者id
     * @param following 被关注者id
     * @return 1：成功， <= 0：失败
     */
    @Delete("delete from user_follow where follower_id = #{follower} and following_id = #{following}")
    Integer delete(@Param("follower") String follower, @Param("following") String following);


    /**
     * 查询是否已经关注（按 follower + following 组合）
     *
     * @param follower  操作者id
     * @param following 被关注人id
     * @return >0 已关注；0 未关注
     */
    @Select("select count(1) from user_follow " +
            "where follower_id = #{follower} and following_id = #{following}")
    Integer existsByPair(@Param("follower") String follower,
                         @Param("following") String following);

    /**
     * 查询关注列表（某人关注了谁）。
     * SQL 里 LEFT JOIN user_follow 一次，得到 viewerId 是否也关注了这一行用户，
     * 写入 [AttentionListModel.isMyFollowing]，前端无须再批量查询。
     *
     * @param follower 列表所属者 uid（要查谁的关注列表）
     * @param viewerId 当前查看者 uid（用于计算 isMyFollowing），可为空
     * @return 列表数据
     */
    List<AttentionListModel> selectAttentionList(
            @Param("follower") String follower,
            @Param("viewerId") String viewerId
    );

    /**
     * 查询粉丝列表（谁关注了某人）。
     *
     * @param following 列表所属者 uid（要查谁的粉丝列表）
     * @param viewerId  当前查看者 uid（用于计算 isMyFollowing），可为空
     */
    List<AttentionListModel> selectFansList(
            @Param("following") String following,
            @Param("viewerId") String viewerId
    );

    // ----------------------------------------------------------------
    // 物化计数维护（user.followers_count / user.following_count）
    // 关注成功 ⇒ 关注者 following_count +1，被关注者 followers_count +1
    // 取关成功 ⇒ 关注者 following_count -1（不低于 0），被关注者 followers_count -1
    // ----------------------------------------------------------------

    /** 当前用户的「关注数」+1。 */
    @Update("update user set following_count = COALESCE(following_count, 0) + 1 " +
            "where user_id = #{uid}")
    Integer incFollowingCount(@Param("uid") String uid);

    /** 当前用户的「关注数」-1（最低为 0）。 */
    @Update("update user set following_count = " +
            "CASE WHEN COALESCE(following_count, 0) > 0 THEN following_count - 1 ELSE 0 END " +
            "where user_id = #{uid}")
    Integer decFollowingCount(@Param("uid") String uid);

    /** 目标用户的「粉丝数」+1。 */
    @Update("update user set followers_count = COALESCE(followers_count, 0) + 1 " +
            "where user_id = #{uid}")
    Integer incFollowersCount(@Param("uid") String uid);

    /** 目标用户的「粉丝数」-1（最低为 0）。 */
    @Update("update user set followers_count = " +
            "CASE WHEN COALESCE(followers_count, 0) > 0 THEN followers_count - 1 ELSE 0 END " +
            "where user_id = #{uid}")
    Integer decFollowersCount(@Param("uid") String uid);
}




















