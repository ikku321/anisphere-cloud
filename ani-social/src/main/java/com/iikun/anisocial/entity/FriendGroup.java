package com.iikun.anisocial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 好友分组实体类
 */
@Data // Lombok 注解：自动生成 Getter/Setter/ToString 等
@TableName("friend_group") // MyBatis-Plus 注解：指定对应数据库表名
public class FriendGroup implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 主键 ID

    private String userId; // 拥有者 user_id

    private String name; // 分组名

    private LocalDateTime createTime; // 创建时间
}
