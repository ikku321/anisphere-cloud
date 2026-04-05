package com.iikun.anisocial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anisocial.entity.UserReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户举报 Mapper 接口
 */
@Mapper // MyBatis Mapper 注解
public interface UserReportMapper extends BaseMapper<UserReport> {
    // 继承 BaseMapper 即可获得常用的 CRUD 功能
}
