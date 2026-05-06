package com.iikun.anicomment.repository;

import com.iikun.anicomment.entity.CommentLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
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

    /**
     * 批量查询：给定一组评论 ID，找出当前用户已点赞过的记录子集。
     * <p>
     * 用途：进入视频详情页时一次性把"我对这视频下哪些评论点过赞"打包返回给客户端，
     * 避免逐条查询。客户端用返回的 commentId 集合渲染实心心形。
     */
    List<CommentLike> findAllByUserIdAndCommentIdIn(String userId, Collection<String> commentIds);
}
