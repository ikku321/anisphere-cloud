package com.iikun.userservice.controller;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.common.utils.DateTimeUtil;
import com.iikun.userservice.domain.request.UpdateUserAddressRequest;
import com.iikun.userservice.domain.request.UserAddersRequest;
import com.iikun.userservice.entity.UserAddress;
import com.iikun.userservice.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * author iikun
 * time 2025/9/19 23:00
 * version 1.0.0
 * msg: 用户收获地址控制层
 */
@Slf4j
@RestController
@RequestMapping("/user-address")
@Tag(name = "用户收获地址", description = "用于添加和修改用户的收货地址，可以添加多个")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    @Operation(summary = "新增收货地址", description = "用于新增用户的收货地址，可添加多个收货地址")
    @PostMapping("/add")
    public Result<?> addUserAddress(HttpServletRequest request, @RequestBody @Valid UserAddersRequest userAddersRequest) {
        // 获取当前操作用户 uid
        String uid = (String) request.getAttribute("uid");
        log.info("新增收货地址: {}", uid);
        userAddersRequest.setUid(uid);
        userAddressService.add(userAddersRequest);
        return Result.success();
    }

    @Operation(summary = "查询用户收货地址", description = "查询当前用户所有收货地址信息")
    @GetMapping("/find-address")
    public Result<?> getUserAllAddress(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        log.info("{}-该用户: {}, 执行了查询用户收货地址操作!", DateTimeUtil.now(), uid);
        return Result.success(userAddressService.findAllUserAdder(uid));
    }

    @Operation(summary = "删除指定的收货地址", description = "根据id删除指定的收货地址")
    @DeleteMapping("/delete-address")
    public Result<?> deleteUserAddress(@RequestParam(required = true) Integer addressId, HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        // 验证id是否为空
        if (addressId == null) {
            throw new ServiceException("id不能为空!");
        }

        // 验证id是否存在
        if (!userAddressService.findByUserAddress(addressId)) {
            throw new ServiceException("删除失败，该收货地址不存在!");
        }
        // 执行删除操作
        userAddressService.deleteByUserAddress(uid, addressId);
        return Result.success();
    }

    @Operation(summary = "修改收货地址", description = "修改当前用户指定的收货地址")
    @PostMapping("/update-address")
    public Result<?> updateUserAddress(
            HttpServletRequest request,
            @RequestBody @Valid UpdateUserAddressRequest updateUserAddressRequest
    ) {
        String uid = (String) request.getAttribute("uid");
        // 执行修改收货地址信息
        userAddressService.updateUserAddress(uid, updateUserAddressRequest);
        val newUserAddress = userAddressService.findById(uid, updateUserAddressRequest.getAddressId());
        Map<String, Object> newUserAddressMap = new HashMap<>();
        newUserAddressMap.put("UserAddress", newUserAddress);
        return Result.success(newUserAddressMap);
    }
}
























