package com.iikun.anisocial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

/**
 * 好友-分组映射实体类
 */
@Data // Lombok 注解：自动生成 Getter/Setter/ToString 等
@TableName("friend_group_mapping") // MyBatis-Plus 注解：指定对应数据库表名
public class FriendGroupMapping implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 主键 ID

    private Long groupId; // 好友分组 ID (friend_group.id)

    private Long friendRelationId; // 好友关系 ID (friend_relation.id)
}
