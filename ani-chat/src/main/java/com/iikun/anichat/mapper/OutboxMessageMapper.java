package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.OutboxMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 外发消息队列 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface OutboxMessageMapper extends BaseMapper<OutboxMessage> {
}
