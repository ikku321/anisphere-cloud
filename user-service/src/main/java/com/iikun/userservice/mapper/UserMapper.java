package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.dto.UserInfoDTO;
import com.iikun.userservice.entity.User;
import org.apache.ibatis.annotations.*;

import javax.swing.text.StyledEditorKit;
import java.util.List;

/**
 * author iikun
 * time 2025/9/17 14:14
 * version 1.0.0
 * msg: 用户表Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 注册（只需要用户基本信息即可）
     * 必须要有以下内容：
     * 1. 账号（username）
     * 2. 密码（password）
     * 3. 昵称（nickname）
     * 4. 邮箱（email）
     */
    @Insert("INSERT INTO user(user_id, username, password, nickname, phone, email) " +
            "VALUES(#{userId}, #{username}, #{password}, #{nickname}, #{phone}, #{email})")
    int register(User user);

    /**
     * 获取用户信息
     */
    @Select("SELECT * FROM user WHERE username = #{username} OR phone = #{username} LIMIT 1")
    User findByUsername(@Param("username") String username);

    /**
     * 查询用户基本信息
     *
     * @param userid 用户id
     * @return 用户信息
     */
    UserInfoDTO foundUserInfo(@Param("userid") String userid);

    /**
     * 修改当前用户邮箱
     *
     * @param email 邮箱
     * @return 修改状态
     */
    @Update("update user set email = #{email} where user_id = #{userid}")
    int updateUserEmail(@Param("email") String email, @Param("userid") String userid);


    /**
     * 修改当前用户的手机号
     *
     * @param newPhone 新手机号
     * @param userid   当前用户id
     * @return 1
     */
    @Update("update user set phone = #{newPhone} where user_id = #{userid}")
    int updateUserPhone(@Param("newPhone") String newPhone, @Param("userid") String userid);


    /**
     * 修改用户昵称
     *
     * @param newNickname 新昵称
     * @param userid      用户id
     * @return 1
     */
    @Update("update user set nickname = #{newNickname} where user_id = #{userid}")
    int updateUserNickName(@Param("newNickname") String newNickname, @Param("userid") String userid);


    /**
     * 查询用户信息（仅限内部查询处理!）
     *
     * @param userid
     * @return
     */
    @Select("select * from user where user_id = #{userid}")
    User getUserById(@Param("userid") String userid);


    /**
     * 根据用户id查询用户是否存在
     *
     * @param userid 用户id
     * @return 返回状态
     */
    @Select("select count(1) from user where user_id = #{userid}")
    Integer selectByUserId(@Param("userid") String userid);


    /**
     * 修改密码
     *
     * @param encodePwd 加密后的密码
     * @param userid    用户id
     */
    @Update("update user set password = #{encodePwd} where user_id = #{userid}")
    int updateUserPwd(@Param("encodePwd") String encodePwd, @Param("userid") String userid);

    /**
     * -- TODO 暂时废弃
     * 根据uid查询用户基本信息
     *
     * @param uid 用户uid
     * @return 1
     */
    UserInfoDTO findUserByUidInfo(@Param("uid") String uid);

    /**
     * 查询用户的id索引
     *
     * @param uid 用户user_id
     * @return 返回用户的id索引
     */
    @Select("select user.id from user where user_id = #{uid}")
    Integer getFindUidById(@Param("uid") String uid);

    /**
     * 更新用户头像地址
     *
     * @param uid       用户uid
     * @param avatarUrl 头像地址
     */
    @Update("update ani_sphere.user set avatar_url = #{avatarUrl} where user_id = #{uid}")
    int updateByUidAvatarUrl(@Param("uid") String uid, @Param("avatarUrl") String avatarUrl);

    /**
     * 查询所有启用状态用户的 user_id (用于公告广播分发到 notification 表).
     *
     * status = 0 仅取「正常」账号; 1禁言 / 2封禁 / 3注销中 状态的账号不接收广播.
     * 仅返回 user_id 字段, 不暴露其它敏感信息.
     *
     * @return 用户 user_id 列表
     */
    @Select("select user_id from user where status = 0")
    List<String> listAllActiveUserIds();
}















