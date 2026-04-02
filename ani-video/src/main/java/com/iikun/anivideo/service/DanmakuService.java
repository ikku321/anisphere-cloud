package com.iikun.anivideo.service;

import com.iikun.anivideo.entity.DTO.DanmakuDTO;
import com.iikun.anivideo.entity.Danmaku;

import java.util.List;

/**
 * 弹幕业务核心接口
 *
 * 功能说明：
 * 定义弹幕系统的核心业务能力，包括弹幕发送、查询、点赞等操作。
 *
 * 设计原则：
 * - 面向接口编程（便于扩展与解耦）
 * - 业务逻辑与数据访问分离（Service vs Mapper）
 * - 所有方法应具备基础校验与异常控制（由实现类完成）
 *
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 */
public interface DanmakuService {

    /**
     * 发送弹幕
     * <p>
     * 业务流程：
     * 1. 校验弹幕内容（长度、敏感词）
     * 2. 绑定用户ID
     * 3. 设置默认属性（点赞数=0、状态=正常）
     * 4. 写入数据库
     * <p>
     * 可扩展：
     * - 防刷限制（限流）
     * - 敏感词过滤
     * - WebSocket实时推送
     *
     * @param dto 弹幕数据（内容、时间轴、颜色、类型等）
     */
    void sendDanmaku(DanmakuDTO dto);

    /**
     * 获取指定视频的弹幕列表
     * <p>
     * 使用场景：
     * - 视频播放前加载弹幕
     * - 拖动进度条后重新加载
     * <p>
     * 注意：
     * - 应按时间轴（position）升序排序
     * - 推荐后续优化为“分段加载”（提升性能）
     *
     * @param videoId 视频ID
     * @return 弹幕列表（按时间排序）
     */
    List<Danmaku> getDanmakuList(String videoId);

    /**
     * 弹幕点赞
     * <p>
     * 业务逻辑：
     * - 对指定弹幕点赞数 +1
     * <p>
     * 注意：
     * - 当前为基础实现（未限制重复点赞）
     * - 可扩展为“用户点赞记录表”防止重复点赞
     *
     * @param id 弹幕ID
     */
    void like(Long id);
}
