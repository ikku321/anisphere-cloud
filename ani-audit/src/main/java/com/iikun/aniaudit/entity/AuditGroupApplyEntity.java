package com.iikun.aniaudit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/4/3
 * version 1.0.0
 * msg: 审核组申请表实体类（audit_group_apply）
 */
@Data
@Schema(description = "审核组申请表（用户申请成为审核员的记录）")
public class AuditGroupApplyEntity {

    /**
     * 自增主键
     */
    @Schema(description = "自增主键")
    private Long id;

    /**
     * 用户ID（申请人）
     */
    @Schema(description = "申请人用户ID")
    private String userId;

    /**
     * 申请理由
     */
    @Schema(description = "申请理由")
    private String reason;

    /**
     * 审核状态：
     * 0 = 待审核
     * 1 = 通过
     * 2 = 拒绝
     */
    @Schema(description = "审核状态：0待审核 1通过 2拒绝")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
