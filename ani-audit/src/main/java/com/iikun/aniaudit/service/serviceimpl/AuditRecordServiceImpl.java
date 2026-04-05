package com.iikun.aniaudit.service.serviceimpl;

import com.iikun.aniaudit.entity.AuditRecordEntity;
import com.iikun.aniaudit.entity.dto.UserDTO;
import com.iikun.aniaudit.mapper.AuditRecordMapper;
import com.iikun.aniaudit.mapper.AuditTaskMapper;
import com.iikun.aniaudit.service.AuditRecordService;
import com.iikun.aniaudit.service.UserService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频审核记录表业务逻辑接口实现类
 */
@Slf4j
@Service
public class AuditRecordServiceImpl implements AuditRecordService {

    @Resource
    private AuditRecordMapper auditRecordMapper;

    @Resource
    private AuditTaskMapper auditTaskMapper;

    @Resource
    private UserService userService;

    @Override
    public void submit(String videoId, Integer result, String comment) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        if (result == null) throw new ServiceException("审核结果不能为空!");
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (userInfo.getData() == null || userInfo.getData().getUserId() == null) {
                throw new ServiceException("获取用户信息失败!");
            }
            String auditorId = userInfo.getData().getUserId();

            int recordAdded = auditRecordMapper.add(videoId, auditorId, result, comment);
            if (recordAdded <= 0) {
                throw new ServiceException("新增审核记录失败");
            }

            // 尝试完成任务（若已领取则完成；未领取则先领取再完成）
            int completed = auditTaskMapper.complete(videoId, auditorId);
            if (completed <= 0) {
                auditTaskMapper.claim(videoId, auditorId);
                auditTaskMapper.complete(videoId, auditorId);
            }
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public List<AuditRecordEntity> listByVideoId(String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        try {
            return auditRecordMapper.listByVideoId(videoId);
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }
}
