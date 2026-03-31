package com.iikun.anicomment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 评论点赞记录（用于按用户幂等点赞/取消点赞）。
 *
 * 设计要点：
 * - 每个用户对同一条评论只能存在一条点赞记录。
 * - 通过唯一复合索引（commentId + userId）在数据库层保证幂等性。
 */
@Data
@Document(collection = "comment_like")
@CompoundIndex(name = "uk_comment_user", def = "{'commentId': 1, 'userId': 1}", unique = true)
public class CommentLike {

    @Id
    private String id;

    /**
     * 评论 ID。
     */
    private String commentId;

    /**
     * 点赞用户 ID。
     */
    private String userId;

    /**
     * 点赞时间。
     */
    private LocalDateTime createTime;
}
