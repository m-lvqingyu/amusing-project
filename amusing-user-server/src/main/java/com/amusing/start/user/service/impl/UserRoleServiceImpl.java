package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.user.entity.pojo.UserRoleInfo;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.UserRoleInfoMapper;
import com.amusing.start.user.service.UserRoleService;
import com.amusing.start.user.utils.UserCacheUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @since 2024/11/19
 */
@RequiredArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleInfoMapper userRoleInfoMapper;

    private final RedissonClient redissonClient;

    @Override
    public Integer insert(UserRoleInfo userRoleInfo) {
        clearCache(userRoleInfo.getUserId());
        int insert = userRoleInfoMapper.insert(userRoleInfo);
        clearCache(userRoleInfo.getUserId());
        return insert;
    }

    @Override
    public Integer update(UserRoleInfo userRoleInfo) {
        clearCache(userRoleInfo.getUserId());
        int update = userRoleInfoMapper.updateById(userRoleInfo);
        clearCache(userRoleInfo.getUserId());
        return update;
    }

    @Override
    public void invalid(String userId, String operateUserId) {
        UserRoleInfo info = new UserRoleInfo()
                .setIsDel(YesOrNo.NO.getKey())
                .setUpdateBy(operateUserId)
                .setUpdateTime(System.currentTimeMillis());
        LambdaUpdateWrapper<UserRoleInfo> query = new LambdaUpdateWrapper<UserRoleInfo>()
                .eq(UserRoleInfo::getUserId, userId)
                .eq(UserRoleInfo::getIsDel, YesOrNo.YES.getKey());
        clearCache(userId);
        userRoleInfoMapper.update(info, query);
        clearCache(userId);
    }

    @Override
    public List<Integer> getRoleIdList(String userId) {
        RBucket<List<Integer>> bucket = redissonClient.getBucket(UserCacheUtil.userRoleCacheKey(userId));
        List<Integer> roleIdList = bucket.get();
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            return roleIdList;
        }
        LambdaQueryWrapper<UserRoleInfo> query = new LambdaQueryWrapper<>();
        query.eq(UserRoleInfo::getUserId, userId);
        query.eq(UserRoleInfo::getIsDel, YesOrNo.YES.getKey());
        List<UserRoleInfo> infoList = userRoleInfoMapper.selectList(query);
        if (CollectionUtil.isEmpty(infoList)) {
            return new ArrayList<>();
        }
        roleIdList = infoList.stream().map(UserRoleInfo::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }
        bucket.set(roleIdList, UserCacheUtil.USER_ROLE_CACHE_TIMEOUT, TimeUnit.MINUTES);
        return roleIdList;
    }

    @Override
    public Boolean bind(Integer roleId) {
        LambdaUpdateWrapper<UserRoleInfo> query = new LambdaUpdateWrapper<>();
        query.eq(UserRoleInfo::getRoleId, roleId);
        query.eq(UserRoleInfo::getIsDel, YesOrNo.YES.getKey());
        return userRoleInfoMapper.selectCount(query) > 0;
    }

    private void clearCache(String userId) {
        redissonClient.getBucket(UserCacheUtil.userRoleCacheKey(userId)).delete();
    }

}
