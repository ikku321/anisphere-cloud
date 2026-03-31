package com.iikun.anicomment.conftroller;

import com.iikun.anicomment.Feign.service.UserService;
import com.iikun.anicomment.dto.CommentPublishRequest;
import com.iikun.anicomment.entity.Comment;
import com.iikun.anicomment.entity.DTO.UserDTO;
import com.iikun.anicomment.service.CommentService;
import com.iikun.anicomment.service.LikeService;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


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

    private final CommentService commentService;

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
}
