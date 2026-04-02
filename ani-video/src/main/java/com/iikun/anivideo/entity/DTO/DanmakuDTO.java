package com.iikun.anivideo.entity.DTO;

import lombok.Data;

/**
 * 弹幕发送数据传输对象（Danmaku Data Transfer Object）
 *
 * <p>用于前端向后端发送弹幕数据时的参数封装</p>
 *
 * <p>字段说明：</p>
 * <ul>
 *     <li>videoId：视频ID，对应弹幕所属视频</li>
 *     <li>content：弹幕内容文本</li>
 *     <li>position：弹幕出现时间（单位：毫秒 ms）</li>
 *     <li>color：弹幕颜色（HEX格式，例如：#FFFFFF）</li>
 *     <li>type：弹幕类型（0：滚动弹幕，1：顶部弹幕，2：底部弹幕）</li>
 *     <li>fontSize：字体大小（如：16、18、24）</li>
 * </ul>
 *
 * <p>示例：</p>
 * <pre>
 * {
 *   "videoId": "v123",
 *   "content": "这段太燃了！",
 *   "position": 15320,
 *   "color": "#FF0000",
 *   "type": 0,
 *   "fontSize": 18
 * }
 * </pre>
 *
 * @author iikun
 * @since 1.0.0
 */
@Data
public class DanmakuDTO {

    /**
     * 视频ID（必填）
     */
    private String videoId;

    /**
     * 弹幕内容（必填，建议长度 <= 100）
     */
    private String content;

    /**
     * 弹幕出现时间（毫秒 ms）
     * 例如：5000 表示视频播放到第5秒出现
     */
    private Long position;

    /**
     * 弹幕颜色（HEX格式）
     * 示例：#FFFFFF（白色）、#FF0000（红色）
     */
    private String color;

    /**
     * 弹幕类型
     * 0：滚动弹幕（默认）
     * 1：顶部固定
     * 2：底部固定
     */
    private Integer type;

    /**
     * 字体大小（单位：px）
     * 常用值：16、18、24
     */
    private Integer fontSize;
}
