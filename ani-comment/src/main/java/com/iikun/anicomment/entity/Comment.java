package com.iikun.anicomment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Data
@Document(collection = "comment")
public class Comment {

    @Id
    private String id;

    private String videoId;

    private String userId;

    private String content;

    private String parentId;   // 父评论ID
    private String replyTo;    // 回复谁的用户ID

    private String rootId;     // 根评论ID（楼主）

    private Integer likes = 0;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean deleted = false;

    private LocalDateTime deleteTime;
}
