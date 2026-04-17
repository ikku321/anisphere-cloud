package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iikun.anivideo.entity.TagEntity;
import com.iikun.anivideo.mapper.TagMapper;
import com.iikun.anivideo.service.TagService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private final TagMapper tagMapper;

    @Override
    public void insertTag(String name, String type) {
        try {
            // 检查标签是否已存在
            boolean existingTag = tagMapper.selectByTagName(name);
            if (existingTag) {
                throw new ServiceException("标签已存在: " + name);
            }

            // 创建新标签
            TagEntity tag = new TagEntity();
            tag.setName(name);
            tag.setType(type);

            int added = tagMapper.add(name);
            if (added <= 0) {
                throw new ServiceException("新增标签失败");
            }
        } catch (DuplicateKeyException e) {
            log.info(e.getMessage());
            throw new ServiceException("标签已存在: " + name);
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void updateTag(Long tagId, String name, String type) {
        try {
            // 检查标签是否存在
            Integer tag = tagMapper.selectById(Integer.parseInt(tagId.toString()));
            if (tag <= 0) {
                throw new ServiceException("标签不存在");
            }
            // 检查名称是否与其他标签重复
            Boolean selectedByTagName = tagMapper.selectByTagName(name);
            if (selectedByTagName) {
                throw new ServiceException("该标签名称已经存在");
            }
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        try {
            // 检查标签是否存在
            Integer tag = tagMapper.selectById(Integer.parseInt(tagId.toString()));
            if (tag <= 0) {
                throw new ServiceException("标签不存在");
            }

            boolean deleted = tagMapper.deleteTagById(Integer.parseInt(tagId.toString()));
            if (!deleted) {
                throw new ServiceException("删除标签失败!");
            }
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<TagEntity> allTag() {
        try {
            List<TagEntity> all = tagMapper.all();
            if (all ==  null || all.isEmpty()) {
                throw new ServiceException("标签列表为空");
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
            List<TagEntity> all = tagMapper.selectByLikeTagName(name);
            if (all == null || all.isEmpty()) {
                throw new ServiceException("标签列表为空!");
            }
            return all;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public TagEntity selectByTagId(Long tagId) {
        try {
            TagEntity tagEntity = tagMapper.foundByTagIdTagEntity(Integer.parseInt(tagId.toString()));
            if (tagEntity == null) {
                throw new ServiceException("未查询到标签内容");
            }
            return tagEntity;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<TagEntity> getTagsByType(String type) {
        try {
            List<TagEntity> tagEntities = tagMapper.selectByTagTypeList(type);
            if (tagEntities == null || tagEntities.isEmpty()) {
                throw new ServiceException("标签列表为空");
            }
            return tagEntities;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }
}
