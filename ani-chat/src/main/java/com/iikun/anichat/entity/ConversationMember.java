package com.iikun.anichat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会话成员表
 *
 * @author iikun
 */
@Data
@TableName("conversation_member")
public class ConversationMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话业务ID
     */
    private String conversationId;

    /**
     * 成员 user_id
     */
    private String userId;

    /**
     * 角色：1成员 2管理员 3群主
     */
    private Integer role;

    /**
     * 加入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinTime;

    /**
     * 若被禁言则记录禁言到期时间
     */
    private LocalDateTime muteUntil;
}
