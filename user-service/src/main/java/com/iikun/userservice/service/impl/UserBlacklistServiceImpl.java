package com.iikun.userservice.service.impl;

import com.iikun.common.base.Result;
import com.iikun.common.common.ServiceException;
import com.iikun.userservice.domain.request.BlackListRequestModel;
import com.iikun.userservice.domain.request.UserBlackListModel;
import com.iikun.userservice.mapper.UserBlacklistMapper;
import com.iikun.userservice.mapper.UserMapper;
import com.iikun.userservice.service.UserBlacklistService;
import com.iikun.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * author iikun
 * time 2025/9/19 23:23
 * version 1.0.0
 * msg: 用户黑名单操作接口定义实现类
 */
@Slf4j
@Service
public class UserBlacklistServiceImpl implements UserBlacklistService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserBlacklistMapper userBlacklistMapper;

    /**
     * 加入黑名单用户
     *
     * @param uid         用户id
     * @param blacklistId 被拉黑对象用户id
     */
    @Override
    public void jionBlack(String uid, String blacklistId) {
        // 查询用户userid转换成索引id
        Integer findUidById = userMapper.getFindUidById(uid);
        if (findUidById == null) throw new ServiceException("未查询到该用户信息[" + uid + "]");
        Integer blackListUserId = userMapper.getFindUidById(blacklistId);
        if (blackListUserId == null) throw new ServiceException("拉黑对象id未查找到[" + blacklistId + "]");

        // 查询数据库拉黑表是否已经存在拉黑对象
        val isExist = userBlacklistMapper.findIsExist(findUidById, blackListUserId);
        if (isExist > 0) throw new ServiceException("不能重复拉黑对象！");

        // 查询数据库存在就存储到数据库表中
        val integer = userBlacklistMapper.joinBlack(findUidById, blackListUserId);
        if (integer <= 0) {
            log.debug("业务: 拉黑失败! code: [" + integer + "]");
            throw new ServiceException("拉黑失败，请重试");
        }
    }


    /**
     * 取消/移除黑名单
     *
     * @param uid         操作用户id
     * @param blacklistId 操作目标id
     */
    @Override
    public void cancelBlackList(String uid, String blacklistId) {
        // 查询用户userid转换成索引id
        Integer findUidById = userMapper.getFindUidById(uid);
        if (findUidById == null) throw new ServiceException("未查询到该用户信息[" + uid + "]");
        Integer blackListUserId = userMapper.getFindUidById(blacklistId);
        if (blackListUserId == null) throw new ServiceException("拉黑对象id未查找到[" + blacklistId + "]");
        log.info("用户提交的数据: 操作对象: {}, 被操作对象: {},", findUidById, blackListUserId);

        // 查询数据库查看是否已经取消拉黑/移除拉黑对象
        val isExist = userBlacklistMapper.findIsExist(findUidById, blackListUserId);
        if (isExist <= 0) throw new ServiceException("拉黑表中不存在该数据!");

        // 执行取消拉黑操作
        val integer = userBlacklistMapper.cancelBlackList(findUidById, blackListUserId);
        if (integer <= 0) {
            throw new ServiceException("移除黑名单失败!");
        }
    }

    /**
     * 查询所有黑名单人员信息（仅限管理员操作）
     */
    @Override
    public List<BlackListRequestModel> all() {
        // 获取数据并返回
        return userBlacklistMapper.findUserBlackListAll();
    }


    /**
     * 查询当前账号下的黑名单列表
     *
     * @param uid 当前账号uid
     */
    @Override
    public List<UserBlackListModel> findBlackList(String uid) {
        val findUidById = userMapper.getFindUidById(uid);
        if (findUidById == null) throw new ServiceException("用户数据不存在!");
        val userBlackList = userBlacklistMapper.findUserBlackList(findUidById);
        if (userBlackList == null) throw new ServiceException("查询数据失败!");
        return userBlackList;
    }

    /**
     * 查询该用户是否已经被拉黑
     *
     * @param uid         当前用户id
     * @param blacklistId 被查询用户id
     */
    @Override
    public String findIsBlackList(String uid, String blacklistId) {
        log.info("数据: uid: {}, blacklistId: {}", uid, blacklistId);
        Integer findUidById = userMapper.getFindUidById(uid);
        if (findUidById == null) throw new ServiceException("当前用户数据异常!");
        val blackList = userBlacklistMapper.isBlackList(String.valueOf(findUidById), blacklistId);
        if (blackList == null) throw new ServiceException("数据为空！");
        log.info("查询是否被拉黑: {}", blackList);
        if (blackList > 0) {
            return "True";
        } else {
            return "False";
        }
    }
}

















