package com.iikun.animessage.modules.announcement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.animessage.modules.announcement.dto.AnnouncementQueryDTO;
import com.iikun.animessage.modules.announcement.dto.AnnouncementRequestDTO;
import com.iikun.animessage.modules.announcement.entity.Announcement;
import com.iikun.animessage.modules.announcement.vo.AnnouncementResponseVO;

/**
 * 公告业务逻辑接口
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 分页查询公告
     */
    Page<AnnouncementResponseVO> getAnnouncementPage(AnnouncementQueryDTO queryDTO);

    /**
     * 获取公告详情
     */
    AnnouncementResponseVO getAnnouncementDetail(String announcementId);

    /**
     * 创建公告
     */
    void createAnnouncement(AnnouncementRequestDTO requestDTO);

    /**
     * 更新公告
     */
    void updateAnnouncement(String announcementId, AnnouncementRequestDTO requestDTO);

    /**
     * 删除公告
     */
    void deleteAnnouncement(String announcementId);

    /**
     * 发布/撤回公告
     */
    void publishAnnouncement(String announcementId, Integer status);
}
