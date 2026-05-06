package com.iikun.anicomment.conftroller;

import com.iikun.anicomment.dto.CommentPublishRequest;
import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.service.LikeService;
import com.iikun.common.base.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;


/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final com.iikun.anicomment.service.CommentService commentService;

    private final LikeService likeService;

    /**
     * 发表评论
     */
    @PostMapping("/publish")
    public Result<Comment> publish(@Valid @RequestBody CommentPublishRequest request) {
        return Result.success(commentService.publish(request));
    }

    /**
     * 分页获取某视频下的一级评论。
     */
    @GetMapping("/list/{videoId}")
    public Result<Page<Comment>> list(@PathVariable String videoId,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return Result.success(commentService.listTopLevelByVideo(videoId, page, size));
    }

    /**
     * 分页获取某根评论下的全部回复。
     */
    @GetMapping("/replies/{rootId}")
    public Result<Page<Comment>> replies(@PathVariable String rootId,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return Result.success(commentService.listReplies(rootId, page, size));
    }

    /**
     * 删除评论（软删除）。
     */
    @DeleteMapping("/{commentId}")
    public Result<?> delete(@PathVariable String commentId) {
        commentService.delete(commentId);
        return Result.success();
    }

    /**
     * 点赞（幂等）。
     */
    @PostMapping("/{commentId}/like")
    public Result<?> like(@PathVariable String commentId) {
        likeService.like(commentId);
        return Result.success();
    }

    /**
     * 取消点赞（幂等）。
     */
    @PostMapping("/{commentId}/unlike")
    public Result<?> unlike(@PathVariable String commentId) {
        likeService.unlike(commentId);
        return Result.success();
    }

    /**
     * 列出当前用户在指定视频下"已点赞过"的评论 ID 列表。
     * <p>
     * 客户端进入视频详情页时调用一次,用来把列表里相应评论的心形渲染成实心。
     * 未登录返回空数组(后端不抛 401,让游客也能浏览评论)。
     */
    @GetMapping("/my-liked")
    public Result<List<String>> myLiked(@RequestParam String videoId) {
        return Result.success(likeService.listMyLikedCommentIds(videoId));
    }
}
