package com.iikun.animessage.modules.announcement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.animessage.modules.announcement.dto.AnnouncementQueryDTO;
import com.iikun.animessage.modules.announcement.dto.AnnouncementRequestDTO;
import com.iikun.animessage.modules.announcement.entity.Announcement;
import com.iikun.animessage.modules.announcement.mapper.AnnouncementMapper;
import com.iikun.animessage.modules.announcement.service.AnnouncementService;
import com.iikun.animessage.modules.announcement.vo.AnnouncementResponseVO;
import com.iikun.animessage.feign.client.UserFeignClient;
import com.iikun.animessage.feign.entity.dto.UserDTO;
import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.context.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 公告业务逻辑接口实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<AnnouncementResponseVO> getAnnouncementPage(AnnouncementQueryDTO queryDTO) {
        Page<Announcement> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        
        // 搜索关键词
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Announcement::getTitle, queryDTO.getKeyword())
                    .or()
                    .like(Announcement::getContent, queryDTO.getKeyword()));
        }
        
        // 状态筛选
        if (queryDTO.getIsPublished() != null) {
            wrapper.eq(Announcement::getIsPublished, queryDTO.getIsPublished());
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Announcement::getCreateTime);
        
        Page<Announcement> announcementPage = this.page(page, wrapper);
        
        // 转换为 VO
        Page<AnnouncementResponseVO> voPage = new Page<>(announcementPage.getCurrent(), announcementPage.getSize(), announcementPage.getTotal());
        voPage.setRecords(announcementPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList()));
        
        return voPage;
    }

    @Override
    public AnnouncementResponseVO getAnnouncementDetail(String announcementId) {
        Announcement announcement = this.getByAnnouncementId(announcementId);
        return convertToVO(announcement);
    }

    @Override
    public void createAnnouncement(AnnouncementRequestDTO requestDTO) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(requestDTO, announcement);
        
        // 设置业务ID
        announcement.setAnnouncementId(UUID.randomUUID().toString().replace("-", ""));
        
        // 使用 Feign 获取当前用户信息
        Result<UserDTO> userResult = userFeignClient.getUserByToken();
        if (userResult == null || userResult.getCode() != 200 || userResult.getData() == null) {
            log.warn("Feign获取用户信息失败，回退到上下文获取");
            String userId = null;
            if (UserContext.getUser() != null) {
                userId = UserContext.getUser().getUid();
            }
            announcement.setAuthorId(userId != null ? userId : "SYSTEM");
        } else {
            announcement.setAuthorId(userResult.getData().getUserId());
        }
        
        if (!this.save(announcement)) {
            throw new ServiceException("创建公告失败");
        }
    }

    @Override
    public void updateAnnouncement(String announcementId, AnnouncementRequestDTO requestDTO) {
        Announcement announcement = this.getByAnnouncementId(announcementId);
        BeanUtils.copyProperties(requestDTO, announcement);
        
        if (!this.updateById(announcement)) {
            throw new ServiceException("更新公告失败");
        }
    }

    @Override
    public void deleteAnnouncement(String announcementId) {
        Announcement announcement = this.getByAnnouncementId(announcementId);
        if (!this.removeById(announcement.getId())) {
            throw new ServiceException("删除公告失败");
        }
    }

    @Override
    public void publishAnnouncement(String announcementId, Integer status) {
        Announcement announcement = this.getByAnnouncementId(announcementId);
        announcement.setIsPublished(status);
        
        if (!this.updateById(announcement)) {
            throw new ServiceException("操作失败");
        }
    }

    private Announcement getByAnnouncementId(String announcementId) {
        Announcement announcement = this.getOne(new LambdaQueryWrapper<Announcement>()
                .eq(Announcement::getAnnouncementId, announcementId));
        if (announcement == null) {
            throw new ServiceException("公告不存在");
        }
        return announcement;
    }

    private AnnouncementResponseVO convertToVO(Announcement announcement) {
        AnnouncementResponseVO vo = new AnnouncementResponseVO();
        BeanUtils.copyProperties(announcement, vo);
        return vo;
    }
}
