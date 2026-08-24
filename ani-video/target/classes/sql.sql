-- 添加新的视频信息
insert into ani_sphere.video(video_id, user_id, title, description, cover_url, video_url, duration, status, visible,
                             price, audit_status)
    VALUE ('test01', 'user1', '测试视频标题', '视频简介', '视频封面地址',
           '视频链接地址', 1000, 0, 1, 0, 0);

-- 视频分片上传临时表
-- 用于记录断点续传的分片状态；分片合并或任务清理后会删除对应记录。
CREATE TABLE IF NOT EXISTS ani_sphere.video_chunk
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    upload_id   VARCHAR(80)  NOT NULL COMMENT '上传任务ID',
    video_id    VARCHAR(80)  NULL COMMENT '业务视频ID，可为空',
    chunk_index INT          NOT NULL COMMENT '分片序号，从0开始',
    chunk_path  VARCHAR(500) NOT NULL COMMENT '分片文件服务器路径',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '0未上传 1已上传',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_upload_chunk (upload_id, chunk_index),
    KEY idx_upload_id (upload_id),
    KEY idx_video_id (video_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '视频分片上传临时表';

-- 修改视频可见状态
update ani_sphere.video
set visible = 0
where video_id = 'tqheBj_nQuaP4pmPvQvcEQ';

-- 根据id查询视频信息
select count(1)
from ani_sphere.video
where video_id = 'test01';

-- 修改视频简介
update ani_sphere.video
set description = '测试修改内容'
where video_id = 'test01';

-- 删除视频
delete
from ani_sphere.video
where video_id = '';

-- 根据视频标题查询视频信息
select *
from ani_sphere.video
where title like concat('%', '试修', '%');

-- 新增视频标签
INSERT INTO ani_sphere.tag (name, type)
VALUES
-- 分类
('动漫', 'category'),
('影视', 'category'),
('游戏', 'category'),
-- 风格
('搞笑', 'style'),
('热血', 'style'),
('治愈', 'style'),
-- 主题
('恋爱', 'theme'),
('校园', 'theme'),
('科幻', 'theme');


-- 删除视频标签
delete
from ani_sphere.tag
where id = '9';

-- 模糊查询视频标签名称
select *
from ani_sphere.tag
where name like concat('%', '模糊查询的内容', '%');

-- 根据id获取视频信息
select *
from ani_sphere.video
where video_id = 'tqheBj_nQuaP4pmPvQvcEQ';

-- 为视频添加标签
insert ani_sphere.video_tag(video_id, tag_id) VALUE
    ('mH9rEqzERw-1z6l2OXQczA', 1);

-- 删除视频标签关联
delete
from ani_sphere.video_tag
where id = 1;

-- 查询所有视频标签关联表
select *
from ani_sphere.video_tag;

-- 根据视频id查询视频所关联的标签
select *
from ani_sphere.video_tag
where video_id = 'mH9rEqzERw-1z6l2OXQczA';

-- 根据id查询视频标签关联信息
select *
from ani_sphere.video_tag
where id = 3;

-- 根据标签id查询所有视频标签关联列表
select *
from ani_sphere.video_tag
where tag_id = 2;

-- 根据id查询弹幕
select *
from ani_sphere.danmaku
where id = 1;

-- 根据标签类型查询标签列表
select *
from ani_sphere.tag
where type = '';

-- 根据视频标签查询视频列表信息
SELECT v.*
FROM ani_sphere.video v
         JOIN ani_sphere.video_tag vt ON v.video_id = vt.video_id
WHERE vt.tag_id = 3;








