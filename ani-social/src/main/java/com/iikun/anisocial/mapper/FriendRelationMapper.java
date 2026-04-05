package com.iikun.anisocial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anisocial.entity.FriendRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友关系 Mapper 接口
 */
@Mapper // MyBatis Mapper 注解
public interface FriendRelationMapper extends BaseMapper<FriendRelation> {
    // 继承 BaseMapper 即可获得常用的 CRUD 功能
}
