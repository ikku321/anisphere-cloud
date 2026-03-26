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
            QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            TagEntity existingTag = tagMapper.selectOne(queryWrapper);
            
            if (existingTag != null) {
                throw new ServiceException("标签已存在: " + name);
            }

            // 创建新标签
            TagEntity tag = new TagEntity();
            tag.setName(name);
            tag.setType(type);
            
            int added = tagMapper.insert(tag);
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
            TagEntity tag = tagMapper.selectById(tagId);
            if (tag == null) {
                throw new ServiceException("标签不存在");
            }

            // 检查名称是否与其他标签重复
            if (!tag.getName().equals(name)) {
                QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name", name).ne("id", tagId);
                TagEntity existingTag = tagMapper.selectOne(queryWrapper);
                
                if (existingTag != null) {
                    throw new ServiceException("标签名称已存在: " + name);
                }
            }

            // 更新标签
            tag.setName(name);
            tag.setType(type);
            
            int updated = tagMapper.updateById(tag);
            if (updated <= 0) {
                throw new ServiceException("更新标签失败");
            }
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        try {
            // 查询该标签是否存在
            TagEntity tag = tagMapper.selectById(tagId);
            if (tag == null) {
                throw new ServiceException("标签不存在!");
            }
            
            int deleted = tagMapper.deleteById(tagId);
            if (deleted <= 0) {
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
            QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("id");
            List<TagEntity> all = tagMapper.selectList(queryWrapper);
            
            if (all == null || all.isEmpty()) {
                return new ArrayList<>();
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
            QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", name).orderByAsc("id");
            List<TagEntity> tagEntities = tagMapper.selectList(queryWrapper);
            
            if (tagEntities == null || tagEntities.isEmpty()) {
                return new ArrayList<>();
            }
            return tagEntities;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public TagEntity selectByTagId(Long tagId) {
        try {
            TagEntity tagEntity = tagMapper.selectById(tagId);
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
            QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", type).orderByAsc("id");
            List<TagEntity> tags = tagMapper.selectList(queryWrapper);
            
            if (tags == null || tags.isEmpty()) {
                return new ArrayList<>();
            }
            return tags;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }

    @Override
    public List<Map<String, Object>> getHotTags(Integer limit) {
        try {
            // 这里需要关联查询video_tag表统计使用次数
            // 暂时返回按ID排序的标签
            QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("id").last("LIMIT " + limit);
            List<TagEntity> tags = tagMapper.selectList(queryWrapper);
            
            List<Map<String, Object>> result = new ArrayList<>();
            for (TagEntity tag : tags) {
                Map<String, Object> tagMap = new HashMap<>();
                tagMap.put("id", tag.getId());
                tagMap.put("name", tag.getName());
                tagMap.put("type", tag.getType());
                tagMap.put("usageCount", 0); // 暂时设为0，实际需要统计
                result.add(tagMap);
            }
            
            return result;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            throw new ServiceException("数据库异常");
        }
    }
}
