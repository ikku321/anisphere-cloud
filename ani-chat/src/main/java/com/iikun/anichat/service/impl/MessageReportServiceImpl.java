package com.iikun.anichat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anichat.entity.MessageReport;
import com.iikun.anichat.mapper.MessageReportMapper;
import com.iikun.anichat.service.MessageReportService;
import com.iikun.common.base.Result;
import org.springframework.stereotype.Service;

/**
 * 消息举报服务实现类
 *
 * @author iikun
 */
@Service
public class MessageReportServiceImpl extends ServiceImpl<MessageReportMapper, MessageReport> implements MessageReportService {

    @Override
    public Result<Void> reportMessage(String reporterId, String messageId, String reason) {
        MessageReport report = new MessageReport();
        report.setMessageId(messageId);
        report.setReporterId(reporterId);
        report.setReason(reason);
        report.setStatus(0); // 待处理
        this.save(report);
        return Result.success(null);
    }
}
