package com.iikun.anicomment.service;

import com.iikun.anicomment.Feign.client.NotificationFeignClient;
import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.entity.CommentLike;
import com.iikun.anicomment.entity.DTO.NotificationRequestDTO;
import com.iikun.anicomment.repository.CommentLikeRepository;
import com.iikun.anicomment.repository.CommentRepository;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import com.iikun.common.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Slf4j
@Service
public class LikeService {

    private final MongoTemplate mongoTemplate;

    private final CommentLikeRepository commentLikeRepository;

    private final CommentRepository commentRepository;

    private final NotificationFeignClient notificationFeignClient;

    public LikeService(MongoTemplate mongoTemplate,
                       CommentLikeRepository commentLikeRepository,
                       CommentRepository commentRepository,
                       NotificationFeignClient notificationFeignClient) {
        this.mongoTemplate = mongoTemplate;
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.notificationFeignClient = notificationFeignClient;
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
        LoginUser loginUser = UserContext.getUser();
        if (loginUser == null || loginUser.getUid() == null || loginUser.getUid().isBlank()) {
            throw new ServiceException("未登录或用户信息缺失");
        }
        String uid = loginUser.getUid();

        // 已点赞直接返回，保证幂等。
        if (commentLikeRepository.existsByCommentIdAndUserId(commentId, uid)) {
            return;
        }

        CommentLike like = new CommentLike();
        like.setId(UUID.randomUUID().toString());
        like.setCommentId(commentId);
        like.setUserId(uid);
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

        // 点赞成功后给评论作者发一条「点赞通知」 (category=system).
        // 自己点赞自己不通知. 评论被删除/不存在 也跳过. 失败 try-catch 兜住.
        sendLikeNotification(commentId, uid);
    }

    /**
     * 给评论作者发点赞通知 (category=system). 失败仅 warn 日志, 不阻塞主流程.
     */
    private void sendLikeNotification(String commentId, String likerUid) {
        try {
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null || Boolean.TRUE.equals(comment.getDeleted())) {
                return;
            }
            String authorUid = comment.getUserId();
            if (authorUid == null || authorUid.isBlank() || authorUid.equals(likerUid)) {
                return;
            }
            String preview = comment.getContent() == null ? ""
                    : (comment.getContent().length() > 60
                            ? comment.getContent().substring(0, 60) + "..."
                            : comment.getContent());
            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setTargetUser(authorUid);
            dto.setCategory("system");
            dto.setTitle("有人点赞了你的评论");
            dto.setContent(preview);
            notificationFeignClient.sendNotification(dto);
        } catch (Exception e) {
            log.warn("[LikeService] 发送点赞通知失败, commentId={}, err={}", commentId, e.getMessage());
        }
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
        LoginUser loginUser = UserContext.getUser();
        if (loginUser == null || loginUser.getUid() == null || loginUser.getUid().isBlank()) {
            throw new ServiceException("未登录或用户信息缺失");
        }

        // 先删自己这一行 CommentLike；删 0 条说明本来就没点过，直接 return 保证幂等。
        // 之前这里被注释掉、所有人都共享 -1，会把别人的点赞计数误减。
        long deleted = commentLikeRepository.deleteByCommentIdAndUserId(commentId, loginUser.getUid());
        if (deleted <= 0) {
            return;
        }

        // likes > 0 时才允许 -1，避免出现负数。
        Query query = new Query(Criteria.where("_id").is(commentId)
                .and("deleted").ne(true)
                .and("likes").gt(0));
        Update update = new Update().inc("likes", -1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

    /**
     * 列出当前用户在指定视频下"已点赞过的评论 ID"。
     * <p>
     * 客户端进入视频详情页时调用一次，配合 /comment/list 的结果用于把心形从空心切换成实心。
     * <p>
     * 实现：
     * 1. 先用 Mongo 投影只取该视频下未删除评论的 _id（避免拉一批完整文档）；
     * 2. 再用 (userId, commentId IN ids) 交集查 comment_like 表。
     *
     * @param videoId 视频 ID
     * @return commentId 字符串列表；未登录或无任何点赞时返回空列表
     */
    public List<String> listMyLikedCommentIds(String videoId) {
        LoginUser loginUser = UserContext.getUser();
        if (loginUser == null || loginUser.getUid() == null || loginUser.getUid().isBlank()) {
            return Collections.emptyList();
        }
        if (videoId == null || videoId.isBlank()) {
            return Collections.emptyList();
        }

        // 1) 投影查询：只拉 _id，节省带宽
        Query commentQuery = new Query(Criteria.where("videoId").is(videoId).and("deleted").ne(true));
        commentQuery.fields().include("_id");
        List<Comment> comments = mongoTemplate.find(commentQuery, Comment.class);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> commentIds = comments.stream()
                .map(Comment::getId)
                .filter(id -> id != null && !id.isBlank())
                .toList();
        if (commentIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 2) 在 comment_like 表里取交集
        return commentLikeRepository
                .findAllByUserIdAndCommentIdIn(loginUser.getUid(), commentIds)
                .stream()
                .map(CommentLike::getCommentId)
                .distinct()
                .toList();
    }
}
