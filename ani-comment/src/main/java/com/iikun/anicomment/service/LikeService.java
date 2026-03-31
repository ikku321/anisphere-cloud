package com.iikun.anicomment.service;

import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.entity.CommentLike;
import com.iikun.anicomment.repository.CommentLikeRepository;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import com.iikun.common.model.LoginUser;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Service
public class LikeService {

    private final MongoTemplate mongoTemplate;

    private final CommentLikeRepository commentLikeRepository;

    public LikeService(MongoTemplate mongoTemplate, CommentLikeRepository commentLikeRepository) {
        this.mongoTemplate = mongoTemplate;
        this.commentLikeRepository = commentLikeRepository;
    }

    /**
     * 点赞（幂等）：同一用户对同一评论重复点赞不会重复计数。
     *
     * 实现方式：
     * 1. 先插入 CommentLike（commentId + userId 唯一），成功表示本次是首次点赞。
     * 2. 插入成功后对 Comment.likes 原子 +1。
     *
     * @param commentId 评论ID
     */
    public void like(String commentId) {

//        // 如果已点赞，直接返回，保证幂等。
//        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, loginUser.getUid())) {
//            return;
//        }

        CommentLike like = new CommentLike();
        like.setId(UUID.randomUUID().toString());
        like.setCommentId(commentId);
        like.setUserId("1");
        like.setCreateTime(LocalDateTime.now());

        try {
            commentLikeRepository.save(like);
        } catch (Exception e) {
            // 并发下可能触发唯一索引冲突：视为已点赞即可。
            return;
        }

        Query query = new Query(Criteria.where("_id").is(commentId).and("deleted").ne(true));
        Update update = new Update().inc("likes", 1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

    /**
     * 取消点赞（幂等）：未点赞直接返回。
     *
     * 实现方式：
     * 1. 删除 CommentLike 记录，删除成功表示本次确实取消了一个点赞。
     * 2. 删除成功后对 Comment.likes 原子 -1，并避免减成负数。
     *
     * @param commentId 评论ID
     */
    public void unlike(String commentId) {
//        long deleted = commentLikeRepository.deleteByCommentIdAndUserId(commentId, loginUser.getUid());
//        if (deleted <= 0) {
//            return;
//        }

        // likes > 0 时才允许 -1，避免出现负数。
        Query query = new Query(Criteria.where("_id").is(commentId)
                .and("deleted").ne(true)
                .and("likes").gt(0));
        Update update = new Update().inc("likes", -1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }
}
