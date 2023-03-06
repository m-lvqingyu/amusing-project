package com.amusing.start.user.service.impl;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.CacheKey;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import com.amusing.start.user.entity.pojo.UserRoleInfo;
import com.amusing.start.user.entity.vo.MenuVo;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.mapper.RoleMenuInfoMapper;
import com.amusing.start.user.mapper.UserRoleInfoMapper;
import com.amusing.start.user.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private UserRoleInfoMapper userRoleInfoMapper;

    @Resource
    private RoleMenuInfoMapper roleMenuInfoMapper;

    @Resource
    private MenuInfoMapper menuInfoMapper;

    private final RedissonClient redissonClient;

    private final AntPathMatcher antPathMatcher;

    @Autowired
    MenuServiceImpl(RedissonClient redissonClient, AntPathMatcher antPathMatcher) {
        this.redissonClient = redissonClient;
        this.antPathMatcher = antPathMatcher;
    }

    @PostConstruct
    public void initRoleMenuMapping() {
        List<MenuInfo> menuInfoList = getMenuAll();
        if (CollectionUtils.isEmpty(menuInfoList)) {
            log.warn("[initRoleMenuMapping]-not found menus, init fail");
            return;
        }
        List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.getAll();
        if (CollectionUtils.isEmpty(roleMenuList)) {
            log.warn("[initRoleMenuMapping]-not found role menu mapping, init fail");
            return;
        }
        Map<Integer, List<RoleMenuInfo>> roleMenuMap = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenuInfo::getRoleId));
        Iterator<Map.Entry<Integer, List<RoleMenuInfo>>> iterator = roleMenuMap.entrySet().iterator();
        String cacheKey = CacheKey.roleMenuMapping();
        RMap<Integer, List<String>> rMap = redissonClient.getMap(cacheKey);
        while (iterator.hasNext()) {
            Map.Entry<Integer, List<RoleMenuInfo>> next = iterator.next();
            Integer roleId = next.getKey();
            List<String> pathList = new ArrayList<>();
            List<RoleMenuInfo> infoList = next.getValue();
            for (RoleMenuInfo roleMenuInfo : infoList) {
                Integer menuId = roleMenuInfo.getMenuId();
                for (MenuInfo menuInfo : menuInfoList) {
                    Integer id = menuInfo.getId();
                    String path = menuInfo.getPath();
                    if (menuId.equals(id) && StringUtils.isNotBlank(path)) {
                        pathList.add(path);
                        break;
                    }
                }
            }
            rMap.put(roleId, pathList);
        }
        rMap.expire(CacheKey.SEVEN, TimeUnit.DAYS);
    }

    @Override
    public Boolean matchPath(String userId, String path) throws CustomException {
        List<Integer> userRoleIds = getUserRoleIds(userId);
        if (CollectionUtils.isEmpty(userRoleIds)) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }
        String cacheKey = CacheKey.roleMenuMapping();
        RMap<Integer, List<String>> rMap = redissonClient.getMap(cacheKey);
        for (Integer roleId : userRoleIds) {
            List<String> pathList = rMap.get(roleId);
            if (CollectionUtils.isEmpty(pathList)) {
                continue;
            }
            for (String uri : pathList) {
                if (antPathMatcher.match(path, uri)) {
                    return true;
                }
            }
        }
        throw new CustomException(ErrorCode.PERMISSION_DENIED);
    }

    @Override
    public List<MenuVo> getMenuTree(String userId) {
        // 获取所有菜单集合
        List<MenuInfo> menuList = getMenuAll();
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        // 当前用户关联的菜单ID集合
        List<Integer> roleIds = getUserRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.getRoleMenuList(roleIds);
        if (CollectionUtils.isEmpty(roleMenuList)) {
            return new ArrayList<>();
        }
        List<Integer> menuIds = roleMenuList.stream().map(RoleMenuInfo::getMenuId).collect(Collectors.toList());
        // 过滤
        List<MenuInfo> subMenuList = menuList.stream().filter(i -> menuIds.contains(i.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(subMenuList)) {
            return new ArrayList<>();
        }
        List<MenuVo> menuVoList = new ArrayList<>();
        for (MenuInfo menuInfo : subMenuList) {
            MenuVo menuVo = MenuVo.builder()
                    .id(menuInfo.getId())
                    .name(menuInfo.getName())
                    .nameCode(menuInfo.getNameCode())
                    .type(menuInfo.getType())
                    .component(menuInfo.getComponent())
                    .path(menuInfo.getPath())
                    .priority(menuInfo.getPriority())
                    .icon(menuInfo.getIcon())
                    .description(menuInfo.getDescription())
                    .parentId(menuInfo.getParentId())
                    .level(menuInfo.getLevel())
                    .build();
            menuVoList.add(menuVo);
        }
        return buildMenuTree(menuVoList);
    }

    private List<MenuInfo> getMenuAll() {
        RBucket<List<MenuInfo>> bucket = redissonClient.getBucket(CacheKey.menuKey());
        List<MenuInfo> infoList = bucket.get();
        if (CollectionUtils.isNotEmpty(infoList)) {
            return infoList;
        }
        infoList = menuInfoMapper.getAll();
        if (CollectionUtils.isNotEmpty(infoList)) {
            bucket.set(infoList);
        }
        return infoList;
    }

    private List<Integer> getUserRoleIds(String userId) {
        String cacheKey = CacheKey.userRoleKey(userId);
        RBucket<List<Integer>> bucket = redissonClient.getBucket(cacheKey);
        List<Integer> roleIds = bucket.get();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            return roleIds;
        }
        List<UserRoleInfo> userRoleList = userRoleInfoMapper.getUserRoleList(userId, UserStatus.VALID.getKey());
        if (CollectionUtils.isEmpty(userRoleList)) {
            return new ArrayList<>();
        }
        roleIds = userRoleList.stream().map(UserRoleInfo::getRoleId).collect(Collectors.toList());
        bucket.set(roleIds, CacheKey.ONE, TimeUnit.HOURS);
        return roleIds;
    }

    private static List<MenuVo> buildMenuTree(List<MenuVo> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(CacheKey.MENU_ROOT_ID))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuVo::getPriority).reversed())
                .collect(Collectors.toList());
    }

    private static List<MenuVo> getChildren(MenuVo root, List<MenuVo> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(root.getId()))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuVo::getPriority).reversed())
                .collect(Collectors.toList());
    }

}
