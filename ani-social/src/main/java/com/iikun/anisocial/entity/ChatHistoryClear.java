package com.iikun.anisocial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天记录清除记录实体类
 */
@Data // Lombok 注解：自动生成 Getter/Setter/ToString 等
@TableName("chat_history_clear") // MyBatis-Plus 注解：指定对应数据库表名
public class ChatHistoryClear implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 主键 ID

    private String requester; // 发起清除的用户

    private String conversationId; // 会话 ID

    private LocalDateTime clearTime; // 清除时间

    private String scope; // 清除范围：self/all
}
