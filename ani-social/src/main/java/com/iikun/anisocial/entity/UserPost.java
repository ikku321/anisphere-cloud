package com.iikun.anisocial.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户动态/说说实体类
 */
@Data // Lombok 注解：自动生成 Getter/Setter/ToString 等
@TableName(value = "user_post", autoResultMap = true) // MyBatis-Plus 注解：指定对应数据库表名，并开启自动结果映射以支持 TypeHandler
public class UserPost implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化 ID

    @TableId(value = "id", type = IdType.AUTO) // 主键自增
    private Long id; // 主键 ID

    private String postId; // 业务 ID

    private String userId; // 发表动态的用户 ID

    private String content; // 动态文本内容

    @TableField(typeHandler = JacksonTypeHandler.class) // 使用 MyBatis-Plus 的 JacksonTypeHandler 处理 JSON 字段
    private List<Map<String, Object>> media; // 图片/视频/附件数组（元数据），对应数据库中的 JSON 字段

    private Integer likeCount; // 点赞数

    private Integer commentCount; // 评论数

    private Integer status; // 1=正常 0=删除/隐藏

    private LocalDateTime createTime; // 创建时间
}
