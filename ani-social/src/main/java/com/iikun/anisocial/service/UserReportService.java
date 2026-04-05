package com.iikun.anisocial.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.anisocial.entity.UserReport;
import com.iikun.common.base.Result;

/**
 * 用户举报业务逻辑接口
 */
public interface UserReportService extends IService<UserReport> {

    /**
     * 提交举报
     *
     * @param reporterId 举报人 ID
     * @param targetUser 被举报人 ID
     * @param reason     举报原因
     * @return 结果
     */
    Result<Void> submitReport(String reporterId, String targetUser, String reason);
}
