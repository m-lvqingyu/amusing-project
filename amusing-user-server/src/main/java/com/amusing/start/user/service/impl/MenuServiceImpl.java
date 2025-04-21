package com.amusing.start.user.service.impl;

import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.config.CacheConfig;
import com.amusing.start.user.enums.CacheKey;
import com.amusing.start.user.enums.menu.MenuStatus;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.utils.UserCacheUtil;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.response.MenuResponse;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.service.MenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuInfoMapper menuInfoMapper;

    private final Cache<String, List<MenuInfo>> menuCache;

    private final RedissonClient redissonClient;

    @Override
    public MenuInfo get(Integer id) {
        MenuInfo menuInfo = menuInfoMapper.selectById(id);
        if (menuInfo == null || !menuInfo.getIsDel().equals(YesOrNo.YES.getKey())) {
            throw new CustomException(UserErrorCode.MENU_NOT_FOUND);
        }
        return menuInfo;
    }

    @Override
    public void codeExist(String code) {
        menuExist(MenuInfo::getCode, code);
    }

    @Override
    public void codeExist(Integer id, String code) {
        menuExist(id, MenuInfo::getCode, code);
    }

    @Override
    public void nameExist(String name) {
        menuExist(MenuInfo::getName, name);
    }

    @Override
    public void nameExist(Integer id, String name) {
        menuExist(id, MenuInfo::getName, name);
    }

    @Override
    public void parentExist(Integer parentId) {
        if (parentId == UserCacheUtil.MENU_ROOT_ID) {
            return;
        }
        List<MenuInfo> menuInfoList = menuInfoList(MenuInfo::getId, parentId);
        if (CollectionUtils.isEmpty(menuInfoList) || menuInfoList.size() == 0) {
            throw new CustomException(UserErrorCode.MENU_NOT_FOUND);
        }
    }

    @Override
    public void insert(MenuInfo menuInfo) {
        clearCache();
        int insert = menuInfoMapper.insert(menuInfo);
        clearCache();
        if (insert <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public void update(MenuInfo menuInfo) {
        clearCache();
        int update = menuInfoMapper.updateById(menuInfo);
        clearCache();
        if (update <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public List<MenuInfo> child(Integer id) {
        return menuInfoList(MenuInfo::getParentId, id);
    }

    @Override
    public List<MenuResponse> getMenuTree(List<Integer> menuIds) {
        List<MenuInfo> menuList = getList();
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(menuIds)) {
            return new ArrayList<>();
        }
        List<MenuInfo> subMenuList = menuList.stream().filter(i -> menuIds.contains(i.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subMenuList)) {
            return new ArrayList<>();
        }
        List<MenuResponse> menuVoList = new ArrayList<>();
        for (MenuInfo menuInfo : subMenuList) {
            menuVoList.add(new MenuResponse().setId(menuInfo.getId())
                    .setName(menuInfo.getName())
                    .setCode(menuInfo.getCode())
                    .setType(menuInfo.getType())
                    .setPath(menuInfo.getPath())
                    .setSort(menuInfo.getSort())
                    .setParentId(menuInfo.getParentId()));
        }
        return buildMenuTree(menuVoList);
    }

    @Override
    public List<MenuInfo> getList() {
        List<MenuInfo> menuList = menuCache.get(CacheConfig.MENU_CACHE_KEY, k -> getList());
        if (CollectionUtils.isNotEmpty(menuList)) {
            return menuList;
        }
        RBucket<List<MenuInfo>> bucket = redissonClient.getBucket(CacheKey.MENU_CACHE.getKey());
        List<MenuInfo> menuInfoList = bucket.get();
        if (CollectionUtils.isNotEmpty(menuInfoList)) {
            menuCache.put(CacheConfig.MENU_CACHE_KEY, menuInfoList);
            return menuInfoList;
        }
        menuInfoList = menuInfoMapper.selectList(new LambdaQueryWrapper<MenuInfo>()
                .eq(MenuInfo::getStatus, MenuStatus.EFFECTIVE.getKey())
                .eq(MenuInfo::getIsDel, YesOrNo.YES.getKey()));
        if (CollectionUtils.isEmpty(menuInfoList)) {
            return new ArrayList<>();
        }
        bucket.set(menuInfoList, CacheKey.MENU_CACHE.getDefTimeToLive(), TimeUnit.MINUTES);
        menuCache.put(CacheConfig.MENU_CACHE_KEY, menuInfoList);
        return menuInfoList;
    }

    @Override
    public List<MenuInfo> getList(List<Integer> menuIds) {
        LambdaQueryWrapper<MenuInfo> query = new LambdaQueryWrapper<>();
        query.in(MenuInfo::getId, menuIds);
        return menuInfoMapper.selectList(query);
    }

    private void clearCache() {
        menuCache.cleanUp();
        redissonClient.getBucket(CacheKey.MENU_CACHE.getKey()).delete();
    }

    private void menuExist(SFunction<MenuInfo, ?> column, Object val) {
        List<MenuInfo> menuInfoList = menuInfoList(column, val);
        if (CollectionUtils.isNotEmpty(menuInfoList) && menuInfoList.size() > 0) {
            throw new CustomException(UserErrorCode.MENU_EXIST);
        }
    }

    private void menuExist(Integer id, SFunction<MenuInfo, ?> column, Object val) {
        List<MenuInfo> menuInfoList = menuInfoList(column, val);
        if (CollectionUtils.isEmpty(menuInfoList)) {
            return;
        }
        List<Integer> idList = menuInfoList.stream().map(MenuInfo::getId).collect(Collectors.toList());
        if (!idList.contains(id)) {
            throw new CustomException(UserErrorCode.MENU_EXIST);
        }
    }

    private List<MenuInfo> menuInfoList(SFunction<MenuInfo, ?> column, Object val) {
        LambdaQueryWrapper<MenuInfo> wrapper = new LambdaQueryWrapper<MenuInfo>()
                .eq(MenuInfo::getIsDel, YesOrNo.YES.getKey())
                .eq(column, val);
        return menuInfoMapper.selectList(wrapper);
    }

    private static List<MenuResponse> buildMenuTree(List<MenuResponse> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(UserCacheUtil.MENU_ROOT_ID))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuResponse::getSort).reversed())
                .collect(Collectors.toList());
    }

    private static List<MenuResponse> getChildren(MenuResponse root, List<MenuResponse> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(root.getId()))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuResponse::getSort).reversed())
                .collect(Collectors.toList());
    }

}
