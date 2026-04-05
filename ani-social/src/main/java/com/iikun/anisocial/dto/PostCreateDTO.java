package com.iikun.anisocial.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 创建动态 DTO
 */
@Data // Lombok 注解：自动生成 Getter/Setter
public class PostCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // 序列化 ID

    private String content; // 动态文本内容

    private List<Map<String, Object>> media; // 媒体资源列表 (图片、视频等)
}
