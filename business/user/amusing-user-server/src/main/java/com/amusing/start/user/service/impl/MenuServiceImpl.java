package com.amusing.start.user.service.impl;

import com.amusing.start.constant.CommCacheKey;
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


    @Autowired
    MenuServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @PostConstruct
    public void initRoleMenuMapping() {
        List<MenuInfo> menuInfoList = getMenuAll();
        if (CollectionUtils.isEmpty(menuInfoList)) {
            log.warn("[initRoleMenuMapping]-not found menus, init fail");
            return;
        }
        List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.selectValidAll();
        if (CollectionUtils.isEmpty(roleMenuList)) {
            log.warn("[initRoleMenuMapping]-not found role menu mapping, init fail");
            return;
        }
        Map<Integer, List<RoleMenuInfo>> roleMenuMap = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenuInfo::getRoleId));
        Iterator<Map.Entry<Integer, List<RoleMenuInfo>>> iterator = roleMenuMap.entrySet().iterator();
        String cacheKey = CommCacheKey.roleMenuMapping();
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
    public List<Integer> getRoleIds(String userId) {
        String cacheKey = CacheKey.userRoleKey(userId);
        RBucket<List<Integer>> bucket = redissonClient.getBucket(cacheKey);
        List<Integer> roleIds = bucket.get();
        if (CollectionUtils.isNotEmpty(roleIds)) {
            return roleIds;
        }
        List<UserRoleInfo> userRoleList = userRoleInfoMapper.selectUserRoles(userId, UserStatus.VALID.getKey());
        if (CollectionUtils.isEmpty(userRoleList)) {
            return new ArrayList<>();
        }
        roleIds = userRoleList.stream().map(UserRoleInfo::getRoleId).collect(Collectors.toList());
        bucket.set(roleIds, CacheKey.ONE, TimeUnit.HOURS);
        return roleIds;
    }

    @Override
    public List<MenuVo> getMenuTree(String userId) {
        // 获取所有菜单集合
        List<MenuInfo> menuList = getMenuAll();
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        // 当前用户关联的菜单ID集合
        List<Integer> roleIds = getRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.selectByRoleIds(roleIds);
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
                    .code(menuInfo.getCode())
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
        return menuInfoMapper.selectValidAll();
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
