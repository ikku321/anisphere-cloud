package com.iikun.userservice.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * author iikun
 * time 2025/10/22 23:40
 * version 1.0.0
 * msg:
 */
@Data
@Valid
public class UserAddersRequest {
    // 用户uid
    private String uid;
    // 收货人姓名
    @NotBlank(message = "收货人姓名不能为空？")
    private String receiverName;
    // 收货人号码
    @NotBlank(message = "收货人号码不能为空？")
    private String receiverPhone;
    // 省份
    @NotBlank(message = "省份不能为空？")
    private String province;
    // 市
    @NotBlank(message = "市区不能为空？")
    private String city;
    // 区/县
    @NotBlank(message = "区/县不能为空？")
    private String district;
    // 详细信息
    @NotBlank(message = "详细信息不能为空？")
    private String detailAddress;
    // 是否设置为默认地址（true=默认，false=非默认；为 null 时由 Service 默认置 false）
    private Boolean isDefault;
}
