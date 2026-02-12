package com.iikun.userservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author iikun
 * time 2025/9/19 22:54
 * version 1.0.0
 * msg:
 */
@Data
@TableName("user_address")
public class UserAddress {

    @TableId
    private Long id;

    @TableField("user_id")
    private String userId; // 关联 user.id

    @TableField("receiver_name")
    private String receiverName; // 收货人姓名

    @TableField("receiver_phone")
    private String receiverPhone; // 收货人电话

    @TableField("province")
    private String province; // 省份

    @TableField("city")
    private String city; // 市

    @TableField("district")
    private String district; // 区/县

    @TableField("detail_address")
    private String detailAddress; // 详细地址

    @TableField("is_default")
    private Boolean isDefault = false; // 是否默认地址

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
