package com.iikun.anicomment.service;

import com.iikun.anicomment.Feign.client.NotificationFeignClient;
import com.iikun.anicomment.dto.CommentPublishRequest;
import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.entity.CommentLike;
import com.iikun.anicomment.entity.DTO.NotificationRequestDTO;
import com.iikun.anicomment.repository.CommentRepository;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import com.iikun.common.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final MongoTemplate mongoTemplate;
    private final NotificationFeignClient notificationFeignClient;

    public CommentService(CommentRepository commentRepository,
                          MongoTemplate mongoTemplate,
                          NotificationFeignClient notificationFeignClient) {
        this.commentRepository = commentRepository;
        this.mongoTemplate = mongoTemplate;
        this.notificationFeignClient = notificationFeignClient;
    }

    /**
     * 发表评论 / 回复评论。
     *
     * 规则：
     * - 从 UserContext 获取当前登录用户，将 userId 写入评论。
     * - 一级评论：parentId 为空，rootId = 自己的 id。
     * - 回复评论：parentId 为父评论 id，rootId 继承父评论的 rootId。
     *
     * @param request 发布评论请求
     * @return 保存后的 Comment
     */
    public Comment publish(CommentPublishRequest request) {
        // 从 ThreadLocal 中取出当前登录用户（由 UserContextFilter 解析 JWT 后写入）。
        // 之前这里被注释掉、写死 "1"，导致 mongo.comment.userId 全部是字符串 "1"，
        // 前端点击评论头像跳到资料页时调 /user/find?uid=1 必然失败。
        LoginUser loginUser = UserContext.getUser();
        if (loginUser == null || loginUser.getUid() == null || loginUser.getUid().isBlank()) {
            throw new ServiceException("未登录或用户信息缺失");
        }

        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setVideoId(request.getVideoId());
        comment.setUserId(loginUser.getUid());
        comment.setContent(request.getContent());
        comment.setParentId(request.getParentId());
        comment.setReplyTo(request.getReplyTo());
        comment.setCreateTime(now);
        comment.setUpdateTime(now);

        // 计算 rootId。
        if (comment.getParentId() == null || comment.getParentId().isBlank()) {
            // 一级评论：rootId 指向自己。
            comment.setParentId(null);
            comment.setRootId(comment.getId());
        } else {
            // 回复评论：需要校验父评论存在且未删除。
            Comment parent = commentRepository.findById(comment.getParentId())
                    .orElseThrow(() -> new ServiceException("父评论不存在"));
            if (Boolean.TRUE.equals(parent.getDeleted())) {
                throw new ServiceException("父评论已删除，无法回复");
            }
            comment.setRootId(parent.getRootId());
        }

        Comment saved = commentRepository.save(comment);

        // 评论保存成功后, 给被回复人发一条「回复通知」 (category=message).
        // 一级评论 (parentId=null) 暂不通知视频作者, 避免跨 ani-video 模块依赖, 留待下一阶段.
        // 自己回复自己也不通知 (避免无意义打扰).
        // 通知失败 try-catch 兜住, 主流程不受影响.
        if (saved != null) {
            String replyTo = saved.getReplyTo();
            String myUid = loginUser.getUid();
            if (replyTo != null && !replyTo.isBlank() && !replyTo.equals(myUid)) {
                sendReplyNotification(replyTo, saved.getContent());
            }
        }

        return saved;
    }

    /**
     * 给被回复人发一条「私信类」通知 (category=message). 失败仅 warn 日志, 不阻塞主流程.
     * content 长度做安全裁剪, 防止过长把通知 payload 撑爆.
     */
    private void sendReplyNotification(String targetUser, String replyContent) {
        try {
            String preview = replyContent == null ? ""
                    : (replyContent.length() > 60 ? replyContent.substring(0, 60) + "..." : replyContent);
            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setTargetUser(targetUser);
            dto.setCategory("message");
            dto.setTitle("有人回复了你的评论");
            dto.setContent(preview);
            notificationFeignClient.sendNotification(dto);
        } catch (Exception e) {
            log.warn("[CommentService] 发送回复通知失败, targetUser={}, err={}", targetUser, e.getMessage());
        }
    }

    /**
     * 分页查询某视频下的一级评论。
     *
     * @param videoId 视频 ID
     * @param page    页码（从 1 开始）
     * @param size    每页大小
     * @return Page<Comment>
     */
    public Page<Comment> listTopLevelByVideo(String videoId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 1) - 1, Math.max(size, 1));
        return commentRepository.findByVideoIdAndParentIdIsNullAndDeletedFalseOrderByCreateTimeAsc(videoId, pageable);
    }

    /**
     * 分页查询某根评论下的全部回复。
     *
     * @param rootId 根评论 ID
     * @param page   页码（从 1 开始）
     * @param size   每页大小
     * @return Page<Comment>
     */
    public Page<Comment> listReplies(String rootId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 1) - 1, Math.max(size, 1));
        return commentRepository.findByRootIdAndDeletedFalseOrderByCreateTimeAsc(rootId, pageable);
    }

    /**
     * 软删除评论。
     *
     * 权限：
     * - 评论作者可删除自己的评论。
     * - 管理员可删除任意评论（role 为 admin/ADMIN）。
     *
     * 删除方式：
     * - deleted = true
     * - deleteTime = now
     *
     * @param commentId 评论 ID
     */
    public void delete(String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("评论不存在"));

        if (Boolean.TRUE.equals(comment.getDeleted())) {
            // 幂等：重复删除不报错。
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        comment.setDeleted(true);
        comment.setDeleteTime(now);
        comment.setUpdateTime(now);

        commentRepository.save(comment);
    }

    public Page<Comment> adminPage(String videoId,
                                   String userId,
                                   String rootId,
                                   String parentId,
                                   Boolean deleted,
                                   String keyword,
                                   int page,
                                   int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(Math.min(size, 100), 1);
        Pageable pageable = PageRequest.of(safePage - 1, safeSize);

        Query query = new Query();
        Criteria criteria = new Criteria();

        boolean hasCriteria = false;
        if (videoId != null && !videoId.isBlank()) {
            criteria = criteria.and("videoId").is(videoId);
            hasCriteria = true;
        }
        if (userId != null && !userId.isBlank()) {
            criteria = criteria.and("userId").is(userId);
            hasCriteria = true;
        }
        if (rootId != null && !rootId.isBlank()) {
            criteria = criteria.and("rootId").is(rootId);
            hasCriteria = true;
        }
        if (parentId != null && !parentId.isBlank()) {
            criteria = criteria.and("parentId").is(parentId);
            hasCriteria = true;
        }
        if (deleted != null) {
            criteria = criteria.and("deleted").is(deleted);
            hasCriteria = true;
        }
        if (keyword != null && !keyword.isBlank()) {
            criteria = criteria.and("content").regex(".*" + escapeRegex(keyword.trim()) + ".*", "i");
            hasCriteria = true;
        }

        if (hasCriteria) {
            query.addCriteria(criteria);
        }

        query.with(Sort.by(Sort.Direction.DESC, "createTime"));
        long total = mongoTemplate.count(query, Comment.class);
        query.with(pageable);
        List<Comment> records = mongoTemplate.find(query, Comment.class);
        return new PageImpl<>(records, pageable, total);
    }

    public void adminSoftDelete(String commentId, boolean cascadeRoot) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("评论不存在"));

        if (Boolean.TRUE.equals(comment.getDeleted()) && !cascadeRoot) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (!cascadeRoot) {
            comment.setDeleted(true);
            comment.setDeleteTime(now);
            comment.setUpdateTime(now);
            commentRepository.save(comment);
            return;
        }

        String rootId = comment.getRootId() == null || comment.getRootId().isBlank() ? comment.getId() : comment.getRootId();
        Query query = new Query(Criteria.where("rootId").is(rootId));
        Query updateQuery = query;
        List<Comment> list = mongoTemplate.find(updateQuery, Comment.class);
        for (Comment c : list) {
            if (!Boolean.TRUE.equals(c.getDeleted())) {
                c.setDeleted(true);
                c.setDeleteTime(now);
                c.setUpdateTime(now);
            }
        }
        commentRepository.saveAll(list);
    }

    public void adminRestore(String commentId, boolean cascadeRoot) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("评论不存在"));

        LocalDateTime now = LocalDateTime.now();
        if (!cascadeRoot) {
            if (!Boolean.TRUE.equals(comment.getDeleted())) {
                return;
            }
            comment.setDeleted(false);
            comment.setDeleteTime(null);
            comment.setUpdateTime(now);
            commentRepository.save(comment);
            return;
        }

        String rootId = comment.getRootId() == null || comment.getRootId().isBlank() ? comment.getId() : comment.getRootId();
        Query query = new Query(Criteria.where("rootId").is(rootId));
        List<Comment> list = mongoTemplate.find(query, Comment.class);
        for (Comment c : list) {
            if (Boolean.TRUE.equals(c.getDeleted())) {
                c.setDeleted(false);
                c.setDeleteTime(null);
                c.setUpdateTime(now);
            }
        }
        commentRepository.saveAll(list);
    }

    public void adminUpdateContent(String commentId, String content) {
        if (content == null || content.isBlank()) {
            throw new ServiceException("content不能为空");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("评论不存在"));
        comment.setContent(content.trim());
        comment.setUpdateTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

    public long adminHardDelete(String commentId, boolean cascadeRoot) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ServiceException("评论不存在"));

        if (!cascadeRoot) {
            commentRepository.deleteById(commentId);
            mongoTemplate.remove(new Query(Criteria.where("commentId").is(commentId)), CommentLike.class);
            return 1L;
        }

        String rootId = comment.getRootId() == null || comment.getRootId().isBlank() ? comment.getId() : comment.getRootId();
        Query query = new Query(Criteria.where("rootId").is(rootId));
        List<Comment> list = mongoTemplate.find(query, Comment.class);
        long count = mongoTemplate.count(query, Comment.class);
        mongoTemplate.remove(query, Comment.class);
        for (Comment c : list) {
            mongoTemplate.remove(new Query(Criteria.where("commentId").is(c.getId())), CommentLike.class);
        }
        return count;
    }

    private String escapeRegex(String input) {
        return input.replace("\\", "\\\\")
                .replace(".", "\\.")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("?", "\\?")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]");
    }
}
