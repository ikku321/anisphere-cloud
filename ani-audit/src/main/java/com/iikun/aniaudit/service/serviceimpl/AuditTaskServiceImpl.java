package com.iikun.aniaudit.service.serviceimpl;

import com.iikun.aniaudit.entity.AuditTask;
import com.iikun.aniaudit.entity.dto.UserDTO;
import com.iikun.aniaudit.mapper.AuditTaskMapper;
import com.iikun.aniaudit.service.AuditTaskService;
import com.iikun.aniaudit.service.UserService;
import com.iikun.aniaudit.utils.Util;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核业务接口实现类
 */
@Slf4j
@Service
public class AuditTaskServiceImpl implements AuditTaskService {

    @Resource
    private AuditTaskMapper auditTaskMapper;

    @Resource
    private UserService userService;

    /**
     * 获取所有待审核的视频
     *
     * @return 返回列表
     */
    @Override
    public List<AuditTask> getAuditList() {
        try {
            // 判断用户权限
            assertAdmin();

            // 执行查询任务
            List<AuditTask> auditTasks = auditTaskMapper.all();
            if (auditTasks.isEmpty()) {
                throw new ServiceException("查询数据库为空");
            }
            return auditTasks;
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }


    /**
     * 新增审核任务
     *
     * @param videoId 待审核视频id
     */
    @Override
    public void newAuditTask(String videoId) {
        try {
            // 判断该视频是否已经存在待审核列表
            int selectByVideoId = auditTaskMapper.selectByVideoId(videoId);
            if (selectByVideoId > 0) {
                throw new ServiceException("该视频已经存在? 请勿重复上传审核");
            }

            int added = auditTaskMapper.add(videoId);
            if (added <= 0) {
                throw new ServiceException("新增待审核失败");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void claimTask(String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (userInfo.getData() == null || userInfo.getData().getUserId() == null) {
                throw new ServiceException("获取用户信息失败!");
            }
            String auditorId = userInfo.getData().getUserId();

            int updated = auditTaskMapper.claim(videoId, auditorId);
            if (updated <= 0) {
                throw new ServiceException("领取审核任务失败(任务可能不存在或已被领取)");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void completeTask(String videoId) {
        if (videoId == null) throw new ServiceException("视频id不存在!");
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (userInfo.getData() == null || userInfo.getData().getUserId() == null) {
                throw new ServiceException("获取用户信息失败!");
            }
            String auditorId = userInfo.getData().getUserId();

            int updated = auditTaskMapper.complete(videoId, auditorId);
            if (updated <= 0) {
                throw new ServiceException("完成审核任务失败(请先领取任务)");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> adminPage(Integer pageNum, Integer pageSize, Integer status, String videoId, String auditorId) {
        assertAdmin();
        int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
        int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);
        int offset = (safePageNum - 1) * safePageSize;
        try {
            long total = auditTaskMapper.countByFilter(status, videoId, auditorId);
            List<AuditTask> records = auditTaskMapper.selectPageByFilter(offset, safePageSize, status, videoId, auditorId);
            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", safePageNum);
            result.put("size", safePageSize);
            result.put("pages", (total + safePageSize - 1) / safePageSize);
            return result;
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public AuditTask adminGetByVideoId(String videoId) {
        assertAdmin();
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("视频id不存在!");
        }
        try {
            AuditTask task = auditTaskMapper.selectOneByVideoId(videoId);
            if (task == null) {
                throw new ServiceException("审核任务不存在");
            }
            return task;
        } catch (DataAccessException e) {
            log.info("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void adminAssignAuditor(String videoId, String auditorId) {
        assertAdmin();
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("视频id不存在!");
        }
        if (auditorId == null || auditorId.isEmpty()) {
            throw new ServiceException("auditorId不能为空!");
        }
        try {
            if (auditTaskMapper.selectByVideoId(videoId) <= 0) {
                throw new ServiceException("审核任务不存在");
            }
            int updated = auditTaskMapper.claim(videoId, auditorId);
            if (updated <= 0) {
                throw new ServiceException("分配失败(任务可能不是待审状态)");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void adminForceComplete(String videoId) {
        assertAdmin();
        if (videoId == null || videoId.isEmpty()) {
            throw new ServiceException("视频id不存在!");
        }
        try {
            if (auditTaskMapper.selectByVideoId(videoId) <= 0) {
                throw new ServiceException("审核任务不存在");
            }
            int updated = auditTaskMapper.forceComplete(videoId);
            if (updated <= 0) {
                throw new ServiceException("强制完成失败(任务可能已完成)");
            }
        } catch (DataAccessException e) {
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> adminSummary() {
        assertAdmin();
        try {
            long pending = auditTaskMapper.countByStatus(0);
            long processing = auditTaskMapper.countByStatus(1);
            long done = auditTaskMapper.countByStatus(2);
            Map<String, Object> result = new HashMap<>();
            result.put("pending", pending);
            result.put("processing", processing);
            result.put("done", done);
            result.put("total", pending + processing + done);
            return result;
        } catch (DataAccessException e) {
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    private void assertAdmin() {
        Result<UserDTO> userInfo = userService.getByTokenUserInfo();
        if (userInfo == null || userInfo.getData() == null) {
            throw new ServiceException("获取用户信息失败!");
        }
        boolean admin = Util.isUserRoole(Integer.parseInt(userInfo.getData().getRole()));
        if (!admin) {
            throw new ServiceException("权限不足? 需要管理员权限");
        }
    }
}
