package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.MessageReadStatus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息已读/未读状态 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface MessageReadStatusMapper extends BaseMapper<MessageReadStatus> {
}
