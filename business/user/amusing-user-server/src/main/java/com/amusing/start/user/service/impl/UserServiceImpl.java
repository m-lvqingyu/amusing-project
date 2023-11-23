package com.amusing.start.user.service.impl;

import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.mapper.UserInfoMapper;
import com.amusing.start.user.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 用户管理ServiceImpl
 * @since 2022/11/26
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Page<UserInfo> getPage(String name, List<String> userIdList, Integer page, Integer size) {
        Page<UserInfo> of = Page.of(page, size);
        userInfoMapper.getList(of, name, userIdList);
        return of;
    }

    @Override
    public UserInfo getById(String userId, Integer status) {
        return userInfoMapper.getById(userId, status);
    }

    @Override
    public UserInfo getByName(String name) {
        return userInfoMapper.getByName(name);
    }

    @Override
    public Integer nameExist(String name) {
        return userInfoMapper.nameExist(name);
    }

    @Override
    public Integer phoneExist(String phone) {
        return userInfoMapper.phoneExist(phone);
    }

    @Override
    public Integer update(UserInfo userInfo) {
        return userInfoMapper.update(userInfo);
    }

    @Override
    public Integer insert(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

}
