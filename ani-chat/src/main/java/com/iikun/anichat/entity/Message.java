package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息表：存储聊天消息，供历史/漫游/回溯使用
 *
 * @author iikun
 */
@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内部主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务消息ID（雪花/UUID）
     */
    private String messageId;

    /**
     * 所属会话 conversation.conversation_id
     */
    private String conversationId;

    /**
     * 发送者 user_id（系统消息可为NULL或system）
     */
    private String fromUser;

    /**
     * 消息类型：text/image/audio/system/notice/...
     */
    private String type;

    /**
     * 消息内容（文本或序列化的元数据）
     */
    private String content;

    /**
     * 附件元数据（若有），如 URL/大小/格式等 (JSON 格式)
     */
    private String attachment;

    /**
     * 会话内顺序号（用于保证多端一致性、漫游）
     */
    private Long seq;

    /**
     * 是否已撤回：0否 1是
     */
    private Integer recalled;

    /**
     * 是否已逻辑删除（仅数据库层面）：0否 1是
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
