package com.iikun.animessage.modules.notification.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/04/05
 * version 1.0.0
 * msg: 用户通知实体（单用户通知）
 */
@Data
@TableName("notification")
public class Notification {

    /**
     * 自增主键ID（数据库内部使用）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 通知业务ID（唯一标识，对外使用）
     */
    private String notificationId;

    /**
     * 接收者用户ID
     */
    private String targetUser;

    /**
     * 通知分类
     * system = 系统通知
     * notice = 公告通知
     * message = 私信
     * activity = 活动通知
     */
    private String category;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容（JSON/文本/富文本）
     */
    private String content;

    /**
     * 是否已读
     * 0 = 未读
     * 1 = 已读
     */
    private Integer isRead;

    /**
     * 创建时间（自动生成）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
