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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            boolean userRoole = Util.isUserRoole(Integer.parseInt(userInfo.getData().getRole()));
            if (!userRoole) {
                throw new ServiceException("权限不足? 需要管理员权限");
            }

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
}
