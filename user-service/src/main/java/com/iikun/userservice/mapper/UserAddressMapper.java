package com.iikun.userservice.mapper;

import com.iikun.userservice.domain.request.UpdateUserAddressRequest;
import com.iikun.userservice.domain.request.UserAddersRequest;
import com.iikun.userservice.entity.UserAddress;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:03
 * version 1.0.0
 * msg: 用户收货地址Mapper
 */
@Mapper
public interface UserAddressMapper {


    /**
     * 新增收货地址
     *
     * @param uid           用户uid
     * @param receiverName  收货人名称
     * @param receiverPhone 收货人联系方式
     * @param province      省名称
     * @param city          市/区名称
     * @param district      县名称
     * @param detailAddress 详细地址
     * @param isDefault     是否设置为默认地址（没有操作就是默认不是）
     * @return 返回成功状态
     */
    @Insert("insert into user_address(user_id, receiver_name, receiver_phone, province, city, district, detail_address, is_default)" +
            "values (#{uid}, #{receiverName},#{receiverPhone},#{province},#{city},#{district},#{detailAddress},#{isDefault})")
    Integer insert(
            @Param("uid") String uid,
            @Param("receiverName") String receiverName,
            @Param("receiverPhone") String receiverPhone,
            @Param("province") String province,
            @Param("city") String city,
            @Param("district") String district,
            @Param("detailAddress") String detailAddress,
            @Param("isDefault") String isDefault
    );


    /**
     * 查询指定用户的所有收货地址
     *
     * @param uid 指定的用户 uid
     * @return 返回查询到的所有收货地址
     */
    @Select("select * from user_address where user_id = #{uid}")
    List<UserAddress> findAllUserAdder(@Param("uid") String uid);

    /**
     * 查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回状态
     */
    @Select("select count(*) from user_address where id = #{addressId}")
    Boolean findByUserAddress(@Param("addressId") Integer addressId);

    /**
     *
     * 根据id查询收货地址是否存在
     *
     * @param addressId 收货地址id
     * @return 返回数据对象
     */
    @Select("select * from user_address where user_id = #{uid} and id = #{addressId}")
    UserAddress findById(String uid, Integer addressId);

    /**
     * 删除指定的收货地址
     *
     * @param addressId 收货地址id
     * @param userId    用户id
     * @return 返回状态: 1-成功，0-失败
     */
    @Delete("delete from user_address where user_id = #{userId} and id = #{addressId}")
    Integer deleteByUserAddress(@Param("userId") String userId, @Param("addressId") Integer addressId);


    Integer updateAddress(
            @Param("userId") String userId,
            @Param("updateUserAddressRequest") UpdateUserAddressRequest updateUserAddressRequest
    );
}

















