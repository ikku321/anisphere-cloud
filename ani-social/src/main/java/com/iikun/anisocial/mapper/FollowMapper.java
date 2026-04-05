package com.iikun.anisocial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anisocial.entity.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注/粉丝 Mapper 接口
 */
@Mapper // MyBatis Mapper 注解
public interface FollowMapper extends BaseMapper<Follow> {
    // 继承 BaseMapper 即可获得常用的 CRUD 功能
}
