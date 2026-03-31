package com.iikun.anicomment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发布评论/回复评论请求体。
 *
 * 说明：
 * - 一级评论：parentId 为空。
 * - 回复评论：parentId 为被回复的评论ID。
 * - replyTo 可选：表示回复的目标用户ID（用于前端展示“回复 xxx”）。
 */
@Data
public class CommentPublishRequest {

    /**
     * 被评论的视频 ID。
     */
    @NotBlank(message = "videoId 不能为空")
    private String videoId;

    /**
     * 评论内容。
     */
    @NotBlank(message = "content 不能为空")
    private String content;

    /**
     * 父评论 ID（为空表示一级评论）。
     */
    private String parentId;

    /**
     * 回复目标用户 ID（可选）。
     */
    private String replyTo;
}
