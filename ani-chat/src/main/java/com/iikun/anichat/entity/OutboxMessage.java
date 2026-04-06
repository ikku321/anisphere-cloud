package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 外发消息队列/归档
 *
 * @author iikun
 */
@Data
@TableName("outbox_message")
public class OutboxMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类型：official/private/system
     */
    private String msgType;

    /**
     * 目标用户 user_id
     */
    private String targetUser;

    /**
     * 发送到会话
     */
    private String conversationId;

    /**
     * 消息体 (JSON 格式)
     */
    private String payload;

    /**
     * 发送状态：0未发送 1已发送 2失败
     */
    private Integer status;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
