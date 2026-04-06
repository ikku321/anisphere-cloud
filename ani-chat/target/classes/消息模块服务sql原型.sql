-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 消息模块（Message / Chat）表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ======================================================
-- 会话表：支持私聊/群聊/频道
-- type: 1=私聊(两人) 2=群聊 3=频道(广播)
-- ======================================================
CREATE TABLE `conversation`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内部主键',
    `conversation_id` VARCHAR(64) NOT NULL UNIQUE COMMENT '业务会话ID（雪花/UUID）',
    `type`            TINYINT     NOT NULL COMMENT '会话类型：1=私聊 2=群聊 3=频道',
    `title`           VARCHAR(255) DEFAULT NULL COMMENT '群/频道名称（私聊可为空或对方昵称）',
    `owner_id`        VARCHAR(64)  DEFAULT NULL COMMENT '群/频道拥有者/创建者 user_id',
    `extra`           JSON         DEFAULT NULL COMMENT '扩展信息（如群公告、头像等）',
    `create_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `update_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会话表（私聊/群聊/频道）';


-- ======================================================
-- 会话成员表：记录会话中的成员及其权限/角色
-- role: 1=普通成员 2=管理员 3=群主
-- mute_until: 免打扰/禁言截止时间（若支持）
-- ======================================================
CREATE TABLE `conversation_member`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `conversation_id` VARCHAR(64) NOT NULL COMMENT 'conversation.conversation_id',
    `user_id`         VARCHAR(64) NOT NULL COMMENT '成员 user_id',
    `role`            TINYINT          DEFAULT 1 COMMENT '角色：1成员 2管理员 3群主',
    `join_time`       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    `mute_until`      TIMESTAMP   NULL DEFAULT NULL COMMENT '若被禁言则记录禁言到期时间',
    UNIQUE KEY `uk_conv_user` (`conversation_id`, `user_id`),
    INDEX `idx_conv` (`conversation_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会话成员表';


-- ======================================================
-- 消息表：主消息持久化（用于漫游/拉历史）
-- 支持撤回（recalled）、删除（deleted）、类型区分（text/image/...）
-- ======================================================
CREATE TABLE `message`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '内部主键',
    `message_id`      VARCHAR(64) NOT NULL UNIQUE COMMENT '业务消息ID（雪花/UUID）',
    `conversation_id` VARCHAR(64) NOT NULL COMMENT '所属会话 conversation.conversation_id',
    `from_user`       VARCHAR(64) NOT NULL COMMENT '发送者 user_id（系统消息可为NULL或system）',
    `type`            VARCHAR(50) DEFAULT 'text' COMMENT '消息类型：text/image/audio/system/notice/... ',
    `content`         TEXT COMMENT '消息内容（文本或序列化的元数据）',
    `attachment`      JSON        DEFAULT NULL COMMENT '附件元数据（若有），如 URL/大小/格式等',
    `seq`             BIGINT      DEFAULT NULL COMMENT '会话内顺序号（用于保证多端一致性、漫游）',
    `recalled`        TINYINT     DEFAULT 0 COMMENT '是否已撤回：0否 1是（撤回后客户端按需展示）',
    `deleted`         TINYINT     DEFAULT 0 COMMENT '是否已逻辑删除（仅数据库层面）',
    `create_time`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_conv_time` (`conversation_id`, `create_time`),
    INDEX `idx_from_time` (`from_user`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息表：存储聊天消息，供历史/漫游/回溯使用';


-- ======================================================
-- 消息已读/未读状态（多端/多用户）
-- 每行代表某用户对某消息的阅读状态（适用于群/私聊）
-- read_flag: 0未读 1已读
-- ======================================================
CREATE TABLE `message_read_status`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id` VARCHAR(64) NOT NULL COMMENT 'message.message_id',
    `user_id`    VARCHAR(64) NOT NULL COMMENT '接收/阅览用户 user_id',
    `read_flag`  TINYINT          DEFAULT 0 COMMENT '0未读 1已读',
    `read_time`  TIMESTAMP   NULL DEFAULT NULL COMMENT '具体阅读时间（若已读）',
    UNIQUE KEY `uk_msg_user` (`message_id`, `user_id`),
    INDEX `idx_user_unread` (`user_id`, `read_flag`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息已读/未读记录（支持多端漫游）';


-- ======================================================
-- 消息撤回/操作日志（选用）
-- 如果需要保留审计/撤回记录，可以记录在此表
-- ======================================================
CREATE TABLE `message_action_log`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id`  VARCHAR(64) NOT NULL,
    `operator_id` VARCHAR(64) NOT NULL COMMENT '执行操作的用户（撤回者）',
    `action`      VARCHAR(50) NOT NULL COMMENT '操作类型：recall/delete/modify',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '撤回/删除原因（可选）',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息操作审计日志';


-- ======================================================
-- 私聊/官方消息推送表（消息推送的持久化队列/归档）
-- 用于实现 7.1/7.2（发送官方/私聊）和消息撤回/查询等
-- ======================================================
CREATE TABLE `outbox_message`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `msg_type`        VARCHAR(50) NOT NULL COMMENT '类型：official/private/system',
    `target_user`     VARCHAR(64) DEFAULT NULL COMMENT '目标用户 user_id（官方广播可为NULL或为conversation_id）',
    `conversation_id` VARCHAR(64) DEFAULT NULL COMMENT '可选：发送到会话',
    `payload`         JSON        NOT NULL COMMENT '消息体（客户端消费格式）',
    `status`          TINYINT     DEFAULT 0 COMMENT '发送状态：0未发送 1已发送 2失败',
    `retry_count`     INT         DEFAULT 0,
    `create_time`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='外发消息队列/归档（可与消息中间件配合）';


-- ======================================================
-- 消息举报表：记录用户对消息的举报
-- ======================================================
CREATE TABLE `message_report`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `message_id`  VARCHAR(64) NOT NULL,
    `reporter_id` VARCHAR(64) NOT NULL COMMENT '举报人 user_id',
    `reason`      VARCHAR(255) DEFAULT NULL COMMENT '举报原因/详情',
    `status`      TINYINT      DEFAULT 0 COMMENT '处理状态：0待处理 1已处理',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX (`message_id`),
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='消息举报';