package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话表（私聊/群聊/频道）
 *
 * @author iikun
 */
@Data
@TableName(value = "conversation", autoResultMap = true)
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 内部主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 业务会话ID（雪花/UUID）
     */
    private String conversationId;

    /**
     * 会话类型：1=私聊 2=群聊 3=频道
     */
    private Integer type;

    /**
     * 群/频道名称（私聊可为空或对方昵称）
     */
    private String title;

    /**
     * 群/频道拥有者/创建者 user_id
     */
    private String ownerId;

    /**
     * 扩展信息（如群公告、头像等）
     */
    private String extra;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
