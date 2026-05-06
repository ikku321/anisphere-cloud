-- 新增用户测试
INSERT INTO user(user_id, username, phone, password, email)
VALUES (2, 'asd', 130, '123', '2505687454@qq.com');


-- 检查用户是否存在（user_id查询）
select *
from user
where user_id = '2';

-- 查询邮箱是否已经绑定其他账号
select *
from user
where email = '2505687454';

-- 查询号码是否已经绑定
select *
from user
where phone = '1301';

-- 验证登录，需要验证username和phone两个
SELECT *
FROM user
WHERE (username = 'test07' OR phone = 'test07')
  AND password = '$2a$10$klq6wyrvOUqESdsdQU2adO/1ee9MELepcbzPDW1IKnJ7NPvnoxgC2';

-- 获取用户信息
SELECT *
FROM user
WHERE username = 'test01'
   OR phone = 'test01'
LIMIT 1;

-- 查询用户基本信息
select user.user_id,
       user.username,
       user.nickname,
       user.avatar_url,
       user.phone,
       user.email,
       user.exp,
       user.level,
       user.status,
       user.role,
       user.followers_count,
       user.following_count,
       user.birthday,
       user.bio,
       user.gender
from user
where user_id = 'NmQxYTMxNzctNzA5MC00MD';


-- 修改当前用户邮箱
update user
set email = '2222222222@qq.com'
where user_id = 'NmQxYTMxNzctNzA5MC00MD'
limit 1;


-- 修改当前用户手机号
update user
set phone = '12222222222'
where user_id = 'YThkNDdmZDYtNmZjNi00ND';

-- 修改当前用户昵称
update user
set nickname = '666'
where user_id = 'YThkNDdmZDYtNmZjNi00ND';

-- 修改密码
update user
set password = '123'
where user_id = 'YThkNDdmZDYtNmZjNi00ND';

-- 根据uid查询用户是否存在
select count(1)
from user
where user_id = 'MjM3MzNiMTUtNDJiYi00Mm1';

-- 根据uuid查询用户信息
select user.user_id,
       user.username,
       user.nickname,
       user.avatar_url,
       user.phone,
       user.email,
       user.exp,
       user.level,
       user.status,
       user.role,
       user.followers_count,
       user.following_count,
       user.birthday,
       user.bio,
       user.gender
from user
where user_id = 'MjM3MzNiMTUtNDJiYi00Mm';


-- 关注用户
insert into user_follow(follower_id, following_id)
VALUES ('MjM3MzNiMTUtNDJiYi00Mm', '51755bf2-5b55-4752-b25d-409ec11d8661');


-- 查询是否已经关注
select count(1)
from user_follow
where following_id = '';

-- 取消关注
delete
from user_follow
where follower_id = ''
  and following_id = '';

-- 查询关注列表
SELECT uf.id          as attentionId,
       uf.follower_id as followerId, -- 被关注者ID
       u.id           as userId,     -- 被关注者的用户ID
       u.user_id      as userUid,    -- 被关注者的UID
       u.nickname,
       u.avatar_url   as avatarUrl,
       uf.create_time as createTime
FROM user_follow uf
         RIGHT JOIN user u ON uf.following_id = u.user_id -- 关联被关注者的信息
WHERE uf.follower_id = 'YmIzMmMxMGYtMjU2OC00OD' -- 关注者ID = 当前用户
ORDER BY uf.create_time DESC;


-- ============================================================
-- 【运维脚本 / 一次性回填】
-- 旧版「关注/取关」代码没有维护 user.followers_count / user.following_count，
-- 升级到新版后，前端展示已改为 user_follow 实时 COUNT，所以即便不执行下面的脚本
-- 数字也是准确的。但物化字段保留下来用于将来的排行榜/推荐等聚合查询，
-- 建议升级时手动执行一次以下两条 UPDATE 把它们补齐。
-- ============================================================
UPDATE user u
SET following_count = (SELECT COUNT(1) FROM user_follow WHERE follower_id = u.user_id);

UPDATE user u
SET followers_count = (SELECT COUNT(1) FROM user_follow WHERE following_id = u.user_id);


-- 用户收货地址
insert into user_address(user_id, receiver_name, receiver_phone, province, city, district, detail_address,
                         is_default)
values ('467bd41e-f175-466c-8eb8-791857353011', 'name', 'phone', '123', '123', '123', '123', '0');


-- 查询指定用户的所有收货地址
select *
from user_address
where user_id = 'YmIzMmMxMGYtMjU2OC00OD';

-- 查询指定的收货地址是否存在
select count(*)
from user_address
where id = 0;

-- 删除指定的收货地址
delete
from user_address
where user_id = '467bd41e-f175-466c-8eb8-791857353011'
  and id = 1;

-- 查询当前用户指定的收货地址
select *
from user_address
where user_id = 'YmIzMmMxMGYtMjU2OC00OD'
  and id = 4;

-- 将指定的用户列入黑名单
insert user_blacklist(user_id, blocked_user_id)
values (1, 8);

-- 查询指定用户的id索引
select user.id
from user
where user_id = '59e399df-a21f-4eab-9607-f0a354efa57e';

-- 查询拉黑表是否已经存在拉黑对象
select COUNT(1) as count
from user_blacklist
where user_id = '1'
  and blocked_user_id = '8';

-- 移除黑名单操作
delete
from user_blacklist
where user_id = '29'
  and blocked_user_id = '23';

-- 查询所有黑名单操作列表（仅限管理员）
SELECT ub.id,
       ub.user_id,
       u.user_id     as uid,
       u.nickname,
       u.avatar_url,
       ub.blocked_user_id,
       u2.user_id    as blocked_user_uid,
       u2.nickname   as blocked_user_nickname,
       u2.avatar_url as blocked_user_avatar_url,
       ub.create_time
FROM user_blacklist as ub
         LEFT JOIN ani_sphere.user u
                   ON ub.user_id = u.id
         LEFT JOIN ani_sphere.user u2
                   ON ub.blocked_user_id = u2.id;


-- 查询当前账号下的所有黑名单列表
select ub.user_id as uid,
       ub.blocked_user_id as BlackUid,
       u.nickname as NickName,
       u.email as Email
from user_blacklist as ub
         join ani_sphere.user u on u.id = ub.blocked_user_id
where ub.user_id = '30';

-- 查询指定用户是否已经拉黑该用户
select count(1) from user_blacklist where user_id = '30' and blocked_user_id = '23';

-- 登录账号
select count(1) from ani_sphere.user where username = 'test10' and password = '';

-- 验证账号跟密码指定权限为0（0: 管理员权限）
select count(1) from ani_sphere.user where (username = 'test10' and password = '') and role = 0;

-- 更新用户头像地址
update ani_sphere.user set avatar_url = 'http://localhost:8080' where user_id = 'MjM3MzNiMTUtNDJiYi00Mm';







