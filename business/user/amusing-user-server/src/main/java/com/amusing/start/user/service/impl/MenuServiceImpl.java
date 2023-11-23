package com.amusing.start.user.service.impl;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.CacheKey;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.vo.MenuVo;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.service.IMenuService;
import com.amusing.start.user.service.IRoleService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuInfoMapper menuInfoMapper;

    private final IRoleService roleService;

    private final NacosConfigManager nacosConfigManager;

    @Value("${spring.redisson.dataId}")
    private String menuDataId;

    @Value("${spring.redisson.group}")
    private String menuGroup;

    @Value("${amusing.menu.list}")
    private String menuJsonArray;

    @Autowired
    public MenuServiceImpl(IRoleService roleService, NacosConfigManager nacosConfigManager) {
        this.roleService = roleService;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public List<MenuVo> getMenuTree(String userId) throws CustomException {
        // 获取所有菜单集合
        List<MenuInfo> menuList = getMenuAll();
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        // 当前用户关联的菜单ID集合
        List<Integer> roleIds = roleService.getRoleIds(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<Integer> menuIds = roleService.getMenuIds(roleIds);
        if (CollectionUtils.isEmpty(menuIds)) {
            return new ArrayList<>();
        }
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
                    .path(menuInfo.getPath())
                    .order(menuInfo.getOrder())
                    .parentId(menuInfo.getParentId())
                    .level(menuInfo.getLevel())
                    .build();
            menuVoList.add(menuVo);
        }
        return buildMenuTree(menuVoList);
    }

    /**
     * @return 菜单列表 (先从配置中心拉去，拉取不到在从数据库查询)
     * @description: 获取所有的菜单列表
     */
    private List<MenuInfo> getMenuAll() throws CustomException {
        List<MenuInfo> menuInfoList = null;
        if (StringUtils.isNotBlank(menuJsonArray)) {
            menuInfoList = JSONArray.parseArray(menuJsonArray, MenuInfo.class);
        }
        if (CollectionUtils.isNotEmpty(menuInfoList)) {
            return menuInfoList;
        }
        menuInfoList = menuInfoMapper.selectValidAll();
        if (CollectionUtils.isEmpty(menuInfoList)) {
            return new ArrayList<>();
        }
        ConfigService configService = nacosConfigManager.getConfigService();
        try {
            String content = JSONArray.toJSONString(menuInfoList);
            configService.publishConfig(menuDataId, menuGroup, content, ConfigType.PROPERTIES.getType());
        } catch (Exception e) {
            log.error("[GetMenuAll]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return menuInfoList;
    }

    private static List<MenuVo> buildMenuTree(List<MenuVo> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(CacheKey.MENU_ROOT_ID))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuVo::getOrder).reversed())
                .collect(Collectors.toList());
    }

    private static List<MenuVo> getChildren(MenuVo root, List<MenuVo> menuVoList) {
        return menuVoList.stream()
                .filter(i -> i.getParentId().equals(root.getId()))
                .peek(i -> i.setChildren(getChildren(i, menuVoList)))
                .sorted(Comparator.comparing(MenuVo::getOrder).reversed())
                .collect(Collectors.toList());
    }

}
