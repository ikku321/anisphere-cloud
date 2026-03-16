package com.iikun.anivideo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 视频标签表
 *
 * 对应数据库表：tag
 * 用于存储系统中所有可用的视频标签
 * 例如：
 * 搞笑 / 游戏 / 科技评测 / 影视剪辑 / Vlog
 */
@Data
@TableName("tag")
@Schema(description = "视频标签表")
public class TagEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @Schema(description = "标签ID")
    private Long id;

    /**
     * 标签名称
     * 例如：搞笑 / 游戏 / 科技 / 影视剪辑
     */
    @TableField("name")
    @Schema(description = "标签名称")
    private String name;

    /**
     * 标签类型
     * 用于分类标签
     * 例如：
     * category  分类
     * genre     类型
     * theme     主题
     */
    @TableField("type")
    @Schema(description = "标签分类类型")
    private String type;
}
