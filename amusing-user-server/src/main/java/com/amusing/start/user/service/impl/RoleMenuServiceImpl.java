package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import com.amusing.start.user.enums.RoleStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.RoleMenuInfoMapper;
import com.amusing.start.user.service.RoleMenuService;
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
 * @since 2024/11/15
 */
@RequiredArgsConstructor
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Resource
    private RoleMenuInfoMapper roleMenuInfoMapper;

    private final RedissonClient redissonClient;

    @Override
    public void invalid(Integer roleId) {
        clearCache(roleId);
        roleMenuInfoMapper.update(new RoleMenuInfo().setStatus(RoleStatus.INVALID.getKey())
                        .setUpdateTime(System.currentTimeMillis()),
                new LambdaUpdateWrapper<RoleMenuInfo>().eq(RoleMenuInfo::getRoleId, roleId)
                        .eq(RoleMenuInfo::getStatus, RoleStatus.VALID.getKey())
                        .eq(RoleMenuInfo::getIsDel, YesOrNo.YES.getKey()));
        clearCache(roleId);
    }

    @Override
    public void insert(RoleMenuInfo info) {
        clearCache(info.getRoleId());
        roleMenuInfoMapper.insert(info);
        clearCache(info.getRoleId());
    }

    @Override
    public List<Integer> getMenuId(Integer roleId) {
        RBucket<List<Integer>> bucket = redissonClient.getBucket(UserCacheUtil.roleMenuCacheKey(roleId));
        List<Integer> menuIdList = bucket.get();
        if (CollectionUtil.isNotEmpty(menuIdList)) {
            return menuIdList;
        }
        LambdaQueryWrapper<RoleMenuInfo> query = new LambdaQueryWrapper<>();
        query.eq(RoleMenuInfo::getRoleId, roleId);
        query.eq(RoleMenuInfo::getStatus, YesOrNo.YES.getKey());
        query.eq(RoleMenuInfo::getIsDel, YesOrNo.YES.getKey());
        List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.selectList(query);
        if (CollectionUtil.isEmpty(roleMenuList)) {
            return new ArrayList<>();
        }
        menuIdList = roleMenuList.stream().map(RoleMenuInfo::getMenuId).collect(Collectors.toList());
        bucket.set(menuIdList, UserCacheUtil.ROLE_MENU_CACHE_TIMEOUT, TimeUnit.MINUTES);
        return menuIdList;
    }

    @Override
    public Boolean isBind(Integer menuId) {
        LambdaQueryWrapper<RoleMenuInfo> query = new LambdaQueryWrapper<>();
        query.eq(RoleMenuInfo::getMenuId, menuId);
        query.eq(RoleMenuInfo::getStatus, YesOrNo.YES.getKey());
        query.eq(RoleMenuInfo::getIsDel, YesOrNo.YES.getKey());
        return roleMenuInfoMapper.selectCount(query) > 0;
    }

    private void clearCache(Integer roleId) {
        redissonClient.getBucket(UserCacheUtil.roleMenuCacheKey(roleId)).delete();
    }

}
