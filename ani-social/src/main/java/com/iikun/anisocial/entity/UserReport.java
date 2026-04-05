package com.iikun.anisocial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户举报实体类
 */
@Data // Lombok 注解：自动生成 Getter/Setter/ToString 等
@TableName("user_report") // MyBatis-Plus 注解：指定对应数据库表名
public class UserReport implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 主键 ID

    private String reporterId; // 举报人 user_id

    private String targetUser; // 被举报人 user_id

    private String reason; // 举报原因

    private Integer status; // 0=待处理 1=已处理

    private LocalDateTime createTime; // 创建时间
}
