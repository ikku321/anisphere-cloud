package com.iikun.userservice.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * author iikun
 * time 2026/1/5 21:55
 * version 1.0.0
 * msg:
 */
@Data
@Valid
public class UpdateUserAddressRequest {
    // 收货地址id
    private Integer addressId;
    // 用户uid
    private String uid;
    // 收货人姓名
    private String receiverName;
    // 收货人号码
    private String receiverPhone;
    // 省份
    private String province;
    // 市
    private String city;
    // 区/县
    private String district;
    // 详细信息
    private String detailAddress;
    // 是否设置为默认地址（true=默认，false=非默认；为 null 时跳过此字段更新）
    private Boolean isDefault;
}
