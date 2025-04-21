package com.amusing.start.user.service.impl;

import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.UserInfoMapper;
import com.amusing.start.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @since 2022/11/26
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    private final PasswordEncoder passwordEncoder;

    private final RedissonClient redissonClient;

    private static final long TIME_TO_LIVE = 3L;

    @Override
    public Page<UserInfo> page(String name,
                               String phone,
                               Integer sources,
                               Integer status,
                               Integer page,
                               Integer size) {
        LambdaQueryWrapper<UserInfo> query = new LambdaQueryWrapper<>();
        query.eq(UserInfo::getIsDel, YesOrNo.YES.getKey());
        if (StringUtils.isNotBlank(phone)) query.eq(UserInfo::getPhone, phone);
        if (StringUtils.isNotBlank(name)) query.eq(UserInfo::getName, name);
        if (status != null) query.eq(UserInfo::getStatus, status);
        if (sources != null) query.eq(UserInfo::getSources, sources);
        return userInfoMapper.selectPage(Page.of(page, size), query);
    }

    @Override
    public UserInfo getById(String userId) {
        RBucket<UserInfo> bucket = redissonClient.getBucket(userCacheKey(userId));
        UserInfo userInfo = bucket.get();
        if (userInfo != null) {
            return userInfo;
        }
        userInfo = getUser(UserInfo::getId, userId);
        if (userInfo != null) {
            bucket.set(userInfo, TIME_TO_LIVE, TimeUnit.MINUTES);
        }
        return userInfo;
    }

    @Override
    public UserInfo getById(String userId, Integer status) {
        UserInfo userInfo = getById(userId);
        return userInfo != null && userInfo.getStatus().equals(status) ? userInfo : null;
    }

    @Override
    public UserInfo getByName(String name) {
        return getUser(UserInfo::getName, name);
    }

    @Override
    public UserInfo getByName(String name, Integer status) {
        return getUser(UserInfo::getName, name, status);
    }

    @Override
    public UserInfo getByPhone(String phone, Integer status) {
        return getUser(UserInfo::getPhone, phone, status);
    }

    @Override
    public UserInfo getByPhone(String phone) {
        return getUser(UserInfo::getPhone, phone);
    }

    @Override
    public Integer insert(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public Integer updateVersion(String userId, Long origVersion, Long newVersion) {
        LambdaUpdateWrapper<UserInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserInfo::getId, userId)
                .eq(UserInfo::getVersion, origVersion)
                .eq(UserInfo::getStatus, UserStatus.VALID.getKey())
                .eq(UserInfo::getIsDel, YesOrNo.YES.getKey());
        UserInfo updateUserInfo = new UserInfo().setVersion(newVersion).setUpdateTime(System.currentTimeMillis());
        return userInfoMapper.update(updateUserInfo, wrapper);
    }

    @Override
    public Boolean changePw(String userId, String operateUserId, String password) {
        UserInfo userInfo = getById(userId, null);
        if (userInfo == null) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
        password = passwordEncoder.encode(password);
        userInfo.setVersion(userInfo.getVersion() + 1)
                .setPassword(password)
                .setUpdateBy(operateUserId)
                .setUpdateTime(System.currentTimeMillis());
        return userInfoMapper.updateById(userInfo) > 0;
    }

    @Override
    public Boolean update(UserInfo userInfo) {
        userInfo.setVersion(userInfo.getVersion() + 1);
        return userInfoMapper.updateById(userInfo) > 0;
    }

    private UserInfo getUser(SFunction<UserInfo, ?> column, Object val) {
        LambdaQueryWrapper<UserInfo> wrapper = getBaseLambda();
        wrapper.eq(column, val);
        return userInfoMapper.selectOne(wrapper);
    }

    private UserInfo getUser(SFunction<UserInfo, ?> column, Object val, Integer status) {
        LambdaQueryWrapper<UserInfo> wrapper = getBaseLambda();
        wrapper.eq(UserInfo::getStatus, status);
        wrapper.eq(column, val);
        return userInfoMapper.selectOne(wrapper);
    }

    private LambdaQueryWrapper<UserInfo> getBaseLambda() {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getIsDel, YesOrNo.YES.getKey());
        return wrapper;
    }

    private String userCacheKey(String userId) {
        return "am:us:" + userId;
    }

}
