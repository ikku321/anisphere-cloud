package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息操作审计日志
 *
 * @author iikun
 */
@Data
@TableName("message_action_log")
public class MessageActionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * message.message_id
     */
    private String messageId;

    /**
     * 执行操作的用户（撤回者）
     */
    private String operatorId;

    /**
     * 操作类型：recall/delete/modify
     */
    private String action;

    /**
     * 撤回/删除原因（可选）
     */
    private String reason;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
