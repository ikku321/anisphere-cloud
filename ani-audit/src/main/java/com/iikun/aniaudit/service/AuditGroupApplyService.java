package com.iikun.aniaudit.service;

import com.iikun.aniaudit.entity.AuditGroupApplyEntity;

import java.util.List;
import java.util.Map;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 审核组申请表业务逻辑接口定义类
 */
public interface AuditGroupApplyService {

    /**
     * 当前用户提交加入审核组申请（存在待审核申请时不重复提交）
     */
    void submit(String reason);

    /**
     * 当前用户查看自己的申请记录
     */
    List<AuditGroupApplyEntity> listMine();

    /**
     * 管理员：待审核申请列表
     */
    List<AuditGroupApplyEntity> listPendingForAdmin();

    /**
     * 管理员：处理申请（1通过 2拒绝）
     */
    void review(Long id, Integer status);

    Map<String, Object> adminPage(Integer pageNum, Integer pageSize, Integer status, String userId);
}
