package com.iikun.anisocial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anisocial.entity.FriendGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友分组 Mapper 接口
 */
@Mapper // MyBatis Mapper 注解
public interface FriendGroupMapper extends BaseMapper<FriendGroup> {
    // 继承 BaseMapper 即可获得常用的 CRUD 功能
}
