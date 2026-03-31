package com.iikun.anicomment.repository;

import com.iikun.anicomment.entity.CommentLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * 评论点赞记录仓储。
 */
public interface CommentLikeRepository extends MongoRepository<CommentLike, String> {

    /**
     * 查找某用户对某评论的点赞记录。
     */
    Optional<CommentLike> findByCommentIdAndUserId(String commentId, String userId);

    /**
     * 是否已点赞。
     */
    boolean existsByCommentIdAndUserId(String commentId, String userId);

    /**
     * 删除点赞记录（取消点赞）。
     */
    long deleteByCommentIdAndUserId(String commentId, String userId);
}
