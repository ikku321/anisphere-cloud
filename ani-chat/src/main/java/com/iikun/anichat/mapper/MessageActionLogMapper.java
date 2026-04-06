package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.MessageActionLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息操作审计日志 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface MessageActionLogMapper extends BaseMapper<MessageActionLog> {
}
