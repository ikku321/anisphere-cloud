package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.ConversationMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会话成员 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface ConversationMemberMapper extends BaseMapper<ConversationMember> {
}
