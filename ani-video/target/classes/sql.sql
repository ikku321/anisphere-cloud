-- 添加新的视频信息
insert into ani_sphere.video(video_id, user_id, title, description, cover_url, video_url, duration, status, visible,price, audit_status)
    VALUE ('test01', 'user1', '测试视频标题', '视频简介', '视频封面地址',
           '视频链接地址', 1000, 0, 1, 0, 0);

-- 修改视频可见状态
update ani_sphere.video set visible = 0 where video_id = 'tqheBj_nQuaP4pmPvQvcEQ';

-- 根据id查询视频信息
select count(1) from ani_sphere.video where video_id = 'test01';

-- 修改视频简介
update ani_sphere.video set description = '测试修改内容' where video_id = 'test01';