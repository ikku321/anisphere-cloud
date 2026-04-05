package com.iikun.anisocial.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anisocial.entity.UserReport;
import com.iikun.anisocial.mapper.UserReportMapper;
import com.iikun.anisocial.service.UserReportService;
import com.iikun.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户举报业务逻辑实现类
 */
@Slf4j // Lombok 注解：开启日志
@Service // Spring 服务类注解
public class UserReportServiceImpl extends ServiceImpl<UserReportMapper, UserReport> implements UserReportService {

    /**
     * 提交举报
     *
     * @param reporterId 举报人 ID
     * @param targetUser 被举报人 ID
     * @param reason     举报原因
     * @return 操作结果
     */
    @Override
    public Result<Void> submitReport(String reporterId, String targetUser, String reason) {
        // 1. 实例化举报记录
        UserReport report = new UserReport(); // 创建对象
        report.setReporterId(reporterId); // 设置举报人 ID
        report.setTargetUser(targetUser); // 设置被举报人 ID
        report.setReason(reason); // 设置举报原因
        report.setStatus(0); // 默认状态为 0（待处理）

        // 2. 保存举报记录
        boolean saved = this.save(report); // 执行插入操作
        return saved ? Result.success() : Result.failed("提交举报失败"); // 返回操作结果
    }
}
