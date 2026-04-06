package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
