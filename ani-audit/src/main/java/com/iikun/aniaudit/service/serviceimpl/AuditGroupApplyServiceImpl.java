package com.iikun.aniaudit.service.serviceimpl;

import com.iikun.aniaudit.entity.AuditGroupApplyEntity;
import com.iikun.aniaudit.entity.dto.UserDTO;
import com.iikun.aniaudit.mapper.AuditGroupApply;
import com.iikun.aniaudit.service.AuditGroupApplyService;
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
 * msg: 审核组申请表业务逻辑接口实现类
 */
@Slf4j
@Service
public class AuditGroupApplyServiceImpl implements AuditGroupApplyService {

    @Resource
    private AuditGroupApply auditGroupApplyMapper;

    @Resource
    private UserService userService;

    @Override
    public void submit(String reason) {
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (userInfo.getData() == null || userInfo.getData().getUserId() == null) {
                throw new ServiceException("获取用户信息失败!");
            }
            String userId = userInfo.getData().getUserId();
            if (auditGroupApplyMapper.countPendingByUserId(userId) > 0) {
                throw new ServiceException("您已有待审核的申请，请勿重复提交");
            }
            int added = auditGroupApplyMapper.insert(userId, reason);
            if (added <= 0) {
                throw new ServiceException("提交申请失败");
            }
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public List<AuditGroupApplyEntity> listMine() {
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (userInfo.getData() == null || userInfo.getData().getUserId() == null) {
                throw new ServiceException("获取用户信息失败!");
            }
            return auditGroupApplyMapper.listByUserId(userInfo.getData().getUserId());
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public List<AuditGroupApplyEntity> listPendingForAdmin() {
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (!Util.isAdmin(Integer.parseInt(userInfo.getData().getRole()))) {
                throw new ServiceException("权限不足，审核组管理需要管理员权限");
            }
            return auditGroupApplyMapper.listPending();
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public void review(Long id, Integer status) {
        if (id == null) throw new ServiceException("申请ID不能为空!");
        if (status == null || (status != 1 && status != 2)) {
            throw new ServiceException("审核状态无效（仅支持1通过、2拒绝）");
        }
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (!Util.isAdmin(Integer.parseInt(userInfo.getData().getRole()))) {
                throw new ServiceException("权限不足，审批审核组申请需要管理员权限");
            }
            int updated = auditGroupApplyMapper.updateStatus(id, status);
            if (updated <= 0) {
                throw new ServiceException("处理失败（申请不存在或已处理）");
            }
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> adminPage(Integer pageNum, Integer pageSize, Integer status, String userId) {
        try {
            Result<UserDTO> userInfo = userService.getByTokenUserInfo();
            if (!Util.isAdmin(Integer.parseInt(userInfo.getData().getRole()))) {
                throw new ServiceException("权限不足，审核组管理需要管理员权限");
            }

            int safePageNum = pageNum == null || pageNum < 1 ? 1 : pageNum;
            int safePageSize = pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 100);
            int offset = (safePageNum - 1) * safePageSize;

            long total = auditGroupApplyMapper.countByFilter(status, userId);
            List<AuditGroupApplyEntity> records = auditGroupApplyMapper.selectPageByFilter(offset, safePageSize, status, userId);

            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("current", safePageNum);
            result.put("size", safePageSize);
            result.put("pages", (total + safePageSize - 1) / safePageSize);
            return result;
        } catch (DataAccessException e) {
            log.error("数据库异常: {}", e.getMessage());
            throw new ServiceException("数据库异常: " + e.getMessage());
        }
    }
}
