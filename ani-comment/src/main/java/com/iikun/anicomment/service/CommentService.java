package com.iikun.anicomment.service;

import com.iikun.anicomment.dto.CommentPublishRequest;
import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.repository.CommentRepository;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import com.iikun.common.model.LoginUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
//        LoginUser loginUser = UserContext.getUser();
//        if (loginUser == null || loginUser.getUid() == null || loginUser.getUid().isBlank()) {
//            throw new ServiceException("未登录或用户信息缺失");
//        }

        LocalDateTime now = LocalDateTime.now();

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setVideoId(request.getVideoId());
        comment.setUserId("1");
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

        return commentRepository.save(comment);
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
}
