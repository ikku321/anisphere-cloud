package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息已读/未读记录
 *
 * @author iikun
 */
@Data
@TableName("message_read_status")
public class MessageReadStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * message.message_id
     */
    private String messageId;

    /**
     * 接收/阅览用户 user_id
     */
    private String userId;

    /**
     * 0未读 1已读
     */
    private Integer readFlag;

    /**
     * 具体阅读时间（若已读）
     */
    private LocalDateTime readTime;
}
