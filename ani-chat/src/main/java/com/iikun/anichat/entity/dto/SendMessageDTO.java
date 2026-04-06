package com.iikun.anichat.entity.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 发送消息 DTO
 *
 * @author iikun
 */
@Data
public class SendMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 所属会话 ID
     */
    private String conversationId;

    /**
     * 消息类型：text/image/audio/system/notice/...
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 附件元数据（JSON 格式）
     */
    private String attachment;
}
