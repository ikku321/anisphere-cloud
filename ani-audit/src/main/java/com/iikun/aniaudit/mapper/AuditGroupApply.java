package com.iikun.aniaudit.mapper;

import com.iikun.aniaudit.entity.AuditGroupApplyEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * author iikun
 * time 2026/2/13 0:35
 * version 1.0.0
 * msg:
 */
@Mapper
public interface AuditGroupApply {

    @Insert("insert into ani_sphere.audit_group_apply(user_id, reason, status) values(#{userId}, #{reason}, 0)")
    int insert(@Param("userId") String userId, @Param("reason") String reason);

    @Select("select count(1) from ani_sphere.audit_group_apply where user_id = #{userId} and status = 0")
    int countPendingByUserId(@Param("userId") String userId);

    @Select("select * from ani_sphere.audit_group_apply where status = 0 order by create_time desc")
    List<AuditGroupApplyEntity> listPending();

    @Update("update ani_sphere.audit_group_apply set status = #{status} where id = #{id} and status = 0")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("select * from ani_sphere.audit_group_apply where user_id = #{userId} order by create_time desc")
    List<AuditGroupApplyEntity> listByUserId(@Param("userId") String userId);
}
