package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息举报表
 *
 * @author iikun
 */
@Data
@TableName("message_report")
public class MessageReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * message.message_id
     */
    private String messageId;

    /**
     * 举报人 user_id
     */
    private String reporterId;

    /**
     * 举报原因/详情
     */
    private String reason;

    /**
     * 处理状态：0待处理 1已处理
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
