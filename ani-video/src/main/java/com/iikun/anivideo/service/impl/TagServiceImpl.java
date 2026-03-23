package com.iikun.anivideo.service.impl;

import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.mapper.TagMapper;
import com.iikun.anivideo.service.TagService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg: 视频标签表接口实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    public final TagMapper tagMapper;

    @Override
    public void insertTag(String tag) {
        try {
            // 查询标签是否相同
            if (!tagMapper.selectByTagName(tag)) {
                throw new ServiceException("已经存在该标签");
            }
            val added = tagMapper.add(tag);
            if (added <= 0) {
                throw new ServiceException("新增标签失败");
            }
        } catch (DuplicateKeyException e) {
            log.info(e.getMessage());
            throw new ServiceException("标签已存在: " + tag);
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常?");
        }
    }

    @Override
    public void deleteTag(String tagId) {
        try {
            // 查询该标签是否存在
            if (tagMapper.selectById(tagId) <= 0) {
                throw new ServiceException("视频标签不存在!");
            }
            val deleted = tagMapper.deleteTagById(tagId);
            if (deleted <= 0) {
                throw new ServiceException("删除视频标签失败!");
            }
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常?");
        }
    }

    @Override
    public List<TagEntity> allTag() {
        try {
            List<TagEntity> all = tagMapper.all();
            if (all.size() <= 0) {
                throw new ServiceException("未查询到标签");
            }
            return all;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<TagEntity> selectByTagName(String name) {
        try {
            List<TagEntity> tagEntities = tagMapper.selectByLikeTagName(name);
            if (tagEntities.size() <= 0) {
                throw new ServiceException("为查询到视频标签");
            }
            return tagEntities;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
        }
        return List.of();
    }

    @Override
    public TagEntity selectByTagId(String tagId) {
        try {
            val tagEntity = tagMapper.deleteTagByIdEntity(tagId);
            if (tagEntity == null) {
                throw new ServiceException("未查询到标签内容");
            }
            return tagEntity;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常!");
        }
    }


}
