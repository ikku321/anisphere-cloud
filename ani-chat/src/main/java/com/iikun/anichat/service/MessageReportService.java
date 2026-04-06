package com.iikun.anichat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anichat.entity.MessageReport;
import com.iikun.common.base.Result;

/**
 * 消息举报服务接口
 *
 * @author iikun
 */
public interface MessageReportService extends IService<MessageReport> {

    /**
     * 举报消息
     *
     * @param reporterId 举报人 ID
     * @param messageId  消息 ID
     * @param reason     举报原因
     * @return 结果
     */
    Result<Void> reportMessage(String reporterId, String messageId, String reason);
}
