package com.iikun.userservice.service.impl;

import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.request.UpdateUserAddressRequest;
import com.iikun.userservice.domain.request.UserAddersRequest;
import com.iikun.userservice.entity.UserAddress;
import com.iikun.userservice.mapper.UserAddressMapper;
import com.iikun.userservice.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:02
 * version 1.0.0
 * msg: 用户收货地址操作接口实现类
 */
@Slf4j
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 新增用户收货地址
     *
     * @param uaReq 用户收货地址参数模型
     */
    @Override
    public void add(UserAddersRequest uaReq) {
        if (uaReq.getIsDefault().isEmpty()) {
            uaReq.setIsDefault("0");
        }
        Integer inserted = userAddressMapper.insert(
                uaReq.getUid(),
                uaReq.getReceiverName(),
                uaReq.getReceiverPhone(),
                uaReq.getProvince(),
                uaReq.getCity(),
                uaReq.getDistrict(),
                uaReq.getDetailAddress(),
                uaReq.getIsDefault()
        );
        if (inserted <= 0) {
            throw new ServiceException("新增收货地址失败");
        }
    }

    /**
     * 查询指定用户全部收货地址
     *
     * @param uid 当前用户 uid
     */
    @Override
    public List<UserAddress> findAllUserAdder(String uid) {
        log.info("开始执行查询收货地址");
        val allUserAdder = userAddressMapper.findAllUserAdder(uid);
        if (allUserAdder == null) {
            throw new ServiceException("查询失败! ");
        }
        log.info("uid: {}，查询用户收货地址没有数据？", uid);
        return allUserAdder;
    }


    /**
     * 根据id查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回是否存在状态: true存在，false不存在
     */
    @Override
    public boolean findByUserAddress(Integer addressId) {
        return userAddressMapper.findByUserAddress(addressId);
    }

    /**
     *
     * 根据id查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回数据对象
     */
    @Override
    public UserAddress findById(String uid, Integer addressId) {
        return userAddressMapper.findById(uid, addressId);
    }

    /**
     * 删除指定的收货地址id
     *
     * @param addressId 收货地址id
     */
    @Override
    public void deleteByUserAddress(String userid, Integer addressId) {
        val integer = userAddressMapper.deleteByUserAddress(userid, addressId);
        if (integer <= 0) {
            throw new ServiceException("删除失败!");
        }
    }

    /**
     * 根据id修改指定
     *
     * @param updateUserAddressRequest 修改的收货地址信息
     */
    @Override
    public void updateUserAddress(String userId, UpdateUserAddressRequest updateUserAddressRequest) {
        // 查询收货地址id是否存在
        if (!userAddressMapper.findByUserAddress(updateUserAddressRequest.getAddressId())) {
            throw new ServiceException("收货地址id不存在!");
        }

        // 执行修改
        val updateAddress = userAddressMapper.updateAddress(userId, updateUserAddressRequest);
        if (updateAddress <= 0) {
            throw new ServiceException("修改收货地址失败!");
        }
    }
}















