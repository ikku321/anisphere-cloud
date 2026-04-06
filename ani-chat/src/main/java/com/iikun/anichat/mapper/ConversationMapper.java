package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
}
