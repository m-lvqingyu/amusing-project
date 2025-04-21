package com.amusing.start.user.service.impl;

import com.amusing.start.user.entity.pojo.UserPhoneEncrypt;
import com.amusing.start.user.mapper.UserPhoneEncryptMapper;
import com.amusing.start.user.service.UserPhoneEncryptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @since 2024/10/25
 */
@Service
public class UserPhoneEncryptServiceImpl implements UserPhoneEncryptService {

    @Resource
    private UserPhoneEncryptMapper userPhoneEncryptMapper;

    @Override
    public UserPhoneEncrypt get(String userId) {
        LambdaQueryWrapper<UserPhoneEncrypt> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPhoneEncrypt::getUserId, userId);
        return userPhoneEncryptMapper.selectOne(wrapper);
    }

    @Override
    public Integer update(UserPhoneEncrypt encrypt) {
        return userPhoneEncryptMapper.updateById(encrypt);
    }

    @Override
    public Integer save(UserPhoneEncrypt encrypt) {
        return userPhoneEncryptMapper.insert(encrypt);
    }

}
