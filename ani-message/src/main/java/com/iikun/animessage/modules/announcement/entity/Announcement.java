package com.iikun.animessage.modules.announcement.entity;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/04/05
 * version 1.0.0
 * msg: 平台公告实体（全局公告/通知）
 */
@Data
@TableName("announcement")
public class Announcement {

    /**
     * 自增主键ID（数据库内部使用）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 公告业务ID（唯一标识，对外使用）
     */
    private String announcementId;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容（富文本/长文本）
     */
    private String content;

    /**
     * 发布者ID（管理员用户ID）
     */
    private String authorId;

    /**
     * 是否发布
     * 0 = 草稿
     * 1 = 已发布
     */
    private Integer isPublished;

    /**
     * 发布时间（支持定时发布）
     * - 如果为空：表示立即发布
     * - 如果有值：到时间后发布
     */
    private LocalDateTime publishTime;

    /**
     * 创建时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
