package com.iikun.userservice.service;

import com.iikun.common.base.Result;
import com.iikun.userservice.domain.request.UpdateUserAddressRequest;
import com.iikun.userservice.domain.request.UserAddersRequest;
import com.iikun.userservice.entity.UserAddress;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:01
 * version 1.0.0
 * msg: 用户收货地址接口定义
 */
public interface UserAddressService {

    /**
     * 新增用户收货地址
     *
     * @param userAddersRequest 用户收货地址参数模型
     */
    void add(UserAddersRequest userAddersRequest);


    /**
     * 查询指定用户全部收货地址
     *
     * @param uid 当前用户 uid
     */
    List<UserAddress> findAllUserAdder(String uid);

    /**
     * 根据id查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回是否存在状态: true存在，false不存在
     */
    boolean findByUserAddress(Integer addressId);

    /**
     *
     * 根据id查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回数据对象
     */
    UserAddress findById(String uid, Integer addressId);


    /**
     * 删除指定的收货地址id
     *
     * @param addressId 收货地址id
     */
    void deleteByUserAddress(String userid, Integer addressId);

    /**
     * 根据id修改指定
     *
     * @param updateUserAddressRequest 修改的收货地址信息
     */
    void updateUserAddress(String userId, @Valid UpdateUserAddressRequest updateUserAddressRequest);
}
