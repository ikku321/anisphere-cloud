package com.iikun.anicomment.repository;

import com.iikun.anicomment.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByVideoIdOrderByCreateTimeAsc(String videoId);

    List<Comment> findByParentId(String parentId);

    List<Comment> findByRootId(String rootId);

    /**
     * 查询某视频下的一级评论（parentId 为空），并分页。
     */
    Page<Comment> findByVideoIdAndParentIdIsNullAndDeletedFalseOrderByCreateTimeAsc(String videoId, Pageable pageable);

    /**
     * 查询某个根评论下的全部回复（包含二级/多级），并分页。
     */
    Page<Comment> findByRootIdAndDeletedFalseOrderByCreateTimeAsc(String rootId, Pageable pageable);
}
