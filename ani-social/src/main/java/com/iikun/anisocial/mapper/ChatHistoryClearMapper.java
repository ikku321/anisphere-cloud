package com.iikun.anisocial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anisocial.entity.ChatHistoryClear;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天记录清除记录 Mapper 接口
 */
@Mapper // MyBatis Mapper 注解
public interface ChatHistoryClearMapper extends BaseMapper<ChatHistoryClear> {
    // 继承 BaseMapper 即可获得常用的 CRUD 功能
}
