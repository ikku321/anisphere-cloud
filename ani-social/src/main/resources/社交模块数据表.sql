-- ---------------------------------------------------------------------------------------------------------------------
-- TODO 社交模块（Friend / Follow / 动态）表设计
-- ---------------------------------------------------------------------------------------------------------------------
-- ======================================================
-- 好友关系表（双向确认关系）
-- status: 0=待验证 1=已通过 2=拒绝/拉黑
-- ======================================================
CREATE TABLE `friend_relation`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64) NOT NULL COMMENT '发起/拥有者 user_id',
    `friend_id`   VARCHAR(64) NOT NULL COMMENT '好友 user_id',
    `status`      TINYINT      DEFAULT 0 COMMENT '0申请中 1已成为好友 2拒绝/拉黑',
    `remark`      VARCHAR(255) DEFAULT NULL COMMENT '备注名（owner 对 friend 的备注）',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_friend` (`user_id`, `friend_id`),
    INDEX `idx_user` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友关系（双向）';


-- ======================================================
-- 好友分组表（one user 多分组）
-- ======================================================
CREATE TABLE `friend_group`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64)  NOT NULL COMMENT '拥有者 user_id',
    `name`        VARCHAR(100) NOT NULL COMMENT '分组名',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_group` (`user_id`, `name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友分组';


-- ======================================================
-- 好友-分组映射：将好友放入分组
-- ======================================================
CREATE TABLE `friend_group_mapping`
(
    `id`                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    `group_id`           BIGINT NOT NULL COMMENT 'friend_group.id',
    `friend_relation_id` BIGINT NOT NULL COMMENT 'friend_relation.id',
    UNIQUE KEY `uk_group_relation` (`group_id`, `friend_relation_id`),
    INDEX (`group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='好友-分组映射';


-- ======================================================
-- 关注/粉丝（单向关系）
-- ======================================================
CREATE TABLE `follow`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`     VARCHAR(64) NOT NULL COMMENT '发起关注的用户',
    `target_user` VARCHAR(64) NOT NULL COMMENT '被关注用户',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_target` (`user_id`, `target_user`),
    INDEX `idx_target` (`target_user`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='关注/粉丝（单向）';


-- ======================================================
-- 举报好友（对某好友的举报记录）
-- ======================================================
CREATE TABLE `user_report`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `reporter_id` VARCHAR(64) NOT NULL COMMENT '举报人 user_id',
    `target_user` VARCHAR(64) NOT NULL COMMENT '被举报人 user_id',
    `reason`      VARCHAR(255) DEFAULT NULL,
    `status`      TINYINT      DEFAULT 0 COMMENT '0待处理 1已处理',
    `create_time` TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户举报（含举报好友）';


-- ======================================================
-- 清除聊天记录的操作表（逻辑记录，实际删除需异步/谨慎）
-- ======================================================
CREATE TABLE `chat_history_clear`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY,
    `requester`       VARCHAR(64) NOT NULL COMMENT '发起清除的用户',
    `conversation_id` VARCHAR(64) NOT NULL,
    `clear_time`      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    `scope`           VARCHAR(50) DEFAULT 'self' COMMENT '清除范围：self/all（谨慎）'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='聊天记录清除请求记录';


-- ======================================================
-- 用户动态/说说（类似朋友圈）
-- ======================================================
CREATE TABLE `user_post`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`       VARCHAR(64) NOT NULL UNIQUE COMMENT '业务id',
    `user_id`       VARCHAR(64) NOT NULL,
    `content`       TEXT COMMENT '动态文本',
    `media`         JSON      DEFAULT NULL COMMENT '图片/视频/附件数组（元数据）',
    `like_count`    INT       DEFAULT 0,
    `comment_count` INT       DEFAULT 0,
    `status`        TINYINT   DEFAULT 1 COMMENT '1正常 0删除/隐藏',
    `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户动态/说说表';