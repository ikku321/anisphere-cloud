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

    /**
     * 广播通知给所有启用状态的用户 (用于公告发布同步分发到 notification 表).
     *
     * 实现思路:
     *   1. 通过 UserFeignClient 拉所有用户 user_id 列表;
     *   2. 给每个用户构造一条 Notification (independent notificationId);
     *   3. 批量 INSERT.
     *
     * @param category 通知分类 (system/notice/message/activity)
     * @param title    通知标题
     * @param content  通知内容
     * @return 实际成功插入的条数
     */
    long broadcastToAll(String category, String title, String content);
}
