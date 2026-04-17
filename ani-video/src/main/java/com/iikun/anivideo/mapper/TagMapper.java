package com.iikun.anivideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iikun.anivideo.entity.TagEntity;
import org.apache.ibatis.annotations.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * 标签 Mapper
 */
@Mapper
public interface TagMapper {

    /**
     * 新增标签
     *
     * @param tag 标签名称
     * @return 返回状态
     */
    @Insert("INSERT INTO ani_sphere.tag(name) VALUES(#{tag})")
    Integer add(@Param("tag") String tag);

    /**
     * 删除视频标签id
     *
     * @param tagId 视频标签id
     * @return 返回删除成功状态
     */
    @Delete("delete from ani_sphere.tag where id = #{tagId}")
    boolean deleteTagById(@Param("tagId") Integer tagId);

    /**
     * 根据视频标签id查询是否存在
     *
     * @param tagId 视频标签id
     * @return 返回查询状态
     */
    @Select("select count(1) from ani_sphere.tag where id = #{tagId}")
    Integer selectById(@Param("tagId") Integer tagId);

    /**
     * 根据标签名称查询是否存在相同
     *
     * @param name 视频标签
     * @return 返回查询状态
     */
    @Select("select count(1) from ani_sphere.tag where name = #{name}")
    Boolean selectByTagName(@Param("name") String name);

    /**
     * 查询所有标签
     *
     * @return 所有标签列表
     */
    @Select("select * from ani_sphere.tag")
    List<TagEntity> all();

    /**
     * 根据标签名称模糊查询
     *
     * @param name 标签名称
     * @return 返回查询到的标签内容
     */
    @Select("select * from ani_sphere.tag where name like concat('%', #{name}, '%')")
    List<TagEntity> selectByLikeTagName(@Param("name") String name);

    /**
     * 根据标签id查询
     *
     * @param tagId 标签id
     * @return 返回标签内容
     */
    @Select("select * from ani_sphere.tag where id = #{tagId}")
    TagEntity foundByTagIdTagEntity(@Param("tagId") Integer tagId);

    /**
     * 根据标签类型查询标签列表
     *
     * @param type 标签类型
     * @return 标签列表
     */
    @Select("select * from ani_sphere.tag where type = #{type}")
    List<TagEntity> selectByTagTypeList(@Param("type") String type);

}
