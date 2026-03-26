-- 添加新的视频信息
insert into ani_sphere.video(video_id, user_id, title, description, cover_url, video_url, duration, status, visible,
                             price, audit_status)
    VALUE ('test01', 'user1', '测试视频标题', '视频简介', '视频封面地址',
           '视频链接地址', 1000, 0, 1, 0, 0);

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
select * from ani_sphere.tag where name like concat('%', '模糊查询的内容', '%');

-- 根据id获取视频信息
select * from ani_sphere.video where video_id = 'tqheBj_nQuaP4pmPvQvcEQ';

-- 为视频添加标签
insert ani_sphere.video_tag(video_id, tag_id) VALUE
('mH9rEqzERw-1z6l2OXQczA', 1);














