package com.iikun.animessage.modules.notification.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iikun.animessage.modules.notification.dto.NotificationQueryDTO;
import com.iikun.animessage.modules.notification.dto.NotificationRequestDTO;
import com.iikun.animessage.modules.notification.entity.Notification;
import com.iikun.animessage.modules.notification.vo.NotificationResponseVO;

/**
 * 通知业务逻辑接口
 */
public interface NotificationService extends IService<Notification> {

    /**
     * 分页查询通知 (针对当前用户)
     */
    Page<NotificationResponseVO> getNotificationPage(NotificationQueryDTO queryDTO);

    /**
     * 创建并发送通知
     */
    void sendNotification(NotificationRequestDTO requestDTO);

    /**
     * 标记通知为已读
     */
    void markAsRead(String notificationId);

    /**
     * 全部标记为已读
     */
    void markAllAsRead(String userId);

    /**
     * 删除通知
     */
    void deleteNotification(String notificationId);

    /**
     * 获取未读通知数量
     */
    long getUnreadCount(String userId);
}
