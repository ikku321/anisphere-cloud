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
     * 查询是否已经关注
     *
     * @param following 被关注人id
     * @return 返回状态
     */
    @Select("select count(1) from user_follow where following_id = #{following}")
    Integer selectByFollowing(@Param("following") String following);

    /**
     * 查询关注列表
     * 数据展示关注列表的被关注者基本信息包括
     * 昵称
     * 头像
     * 关注时间
     *
     * @param follower 关注者id
     * @return 返回数据列表
     */
    List<AttentionListModel> selectAttentionList(@Param("follower") String follower);
}




















