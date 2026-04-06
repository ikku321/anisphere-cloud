package com.iikun.anichat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anichat.entity.MessageReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息举报 Mapper 接口
 *
 * @author iikun
 */
@Mapper
public interface MessageReportMapper extends BaseMapper<MessageReport> {
}
