package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.CacheKey;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.pojo.RoleInfo;
import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import com.amusing.start.user.entity.pojo.UserRoleInfo;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.amusing.start.user.enums.RoleStatus;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.mapper.RoleInfoMapper;
import com.amusing.start.user.mapper.RoleMenuInfoMapper;
import com.amusing.start.user.mapper.UserRoleInfoMapper;
import com.amusing.start.user.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @description: 角色管理
 * @since 2021/09/21
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Resource
    private RoleMenuInfoMapper roleMenuInfoMapper;

    @Resource
    private UserRoleInfoMapper userRoleInfoMapper;

    private final RedissonClient redissonClient;

    @Autowired
    public RoleServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Value("${amusing.admin.role.id}")
    private Integer adminRoleId;

    /**
     * @param name 角色名称
     * @param code 角色编码
     * @param size 每页展示条目
     * @param page 页码
     * @return 角色列表
     * @description: 角色列表-分页
     */
    @Override
    public IPage<RoleInfoVo> list(String name, String code, Integer size, Integer page) {
        Page<RoleInfoVo> roleInfoPage = Page.of(page, size);
        roleInfoMapper.selectByNameOrCode(roleInfoPage, name, code);
        return roleInfoPage;
    }

    /**
     * @param userId 用户ID
     * @param dto    角色信息
     * @return 角色ID
     * @description: 角色新增或修改
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer add(String userId, RoleAddDto dto) {
        RoleInfo roleInfo = buildRoleInfo(userId, dto);
        Integer update;
        Integer id = dto.getId();
        if (id != null && id > 0) {
            checkRoleExist(id);
            update = roleInfoMapper.update(roleInfo);
        } else {
            update = roleInfoMapper.insert(roleInfo);
        }
        return roleInfo.getId();
    }

    /**
     * @param userId 用户ID
     * @param dto    角色菜单ID
     * @return true:成功  false:失败
     * @description: 角色菜单绑定
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean menuBind(String userId, RoleMenuBindDto dto) {
        Integer roleId = dto.getRoleId();
        checkRoleExist(roleId);
        List<Integer> menuIds = dto.getMenuIds();
        if (CollectionUtil.isEmpty(menuIds)) {
            roleMenuInfoMapper.invalidByRoleId(roleId);
            return true;
        }
        List<MenuInfo> infoList = menuInfoMapper.selectByIds(menuIds);
        if (CollectionUtil.isEmpty(infoList) || infoList.size() != menuIds.size()) {
            throw new CustomException(UserErrorCode.MENU_NOT_FOUND);
        }
        roleMenuInfoMapper.invalidByRoleId(roleId);
        long millis = System.currentTimeMillis();
        for (Integer menuId : menuIds) {
            RoleMenuInfo roleMenu = roleMenuInfoMapper.selectByRIdAndMId(roleId, menuId);
            if (roleMenu != null) {
                roleMenu.setStatus(RoleStatus.VALID.getKey());
                roleMenu.setUpdateBy(userId);
                roleMenu.setUpdateTime(millis);
                Integer update = roleMenuInfoMapper.update(roleMenu);
                continue;
            }
            RoleMenuInfo info = RoleMenuInfo.builder()
                    .roleId(roleId)
                    .menuId(menuId)
                    .status(RoleStatus.VALID.getKey())
                    .isDel(YesOrNo.YES.getKey())
                    .createBy(userId)
                    .createTime(millis)
                    .updateBy(userId)
                    .updateTime(millis)
                    .build();
            Integer insert = roleMenuInfoMapper.insert(info);
        }
        return true;
    }

    /**
     * @return 管理员角色ID
     */
    @Override
    public Integer getAdminRoleId() {
        return adminRoleId;
    }

    /**
     * @param userId 用户ID
     * @return true:是  false:不是
     * @description: 判断用户是否是管理员角色
     */
    @Override
    public Boolean isAdmin(String userId) {
        List<Integer> list = getRoleIds(userId);
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        if (list.contains(adminRoleId)) {
            return true;
        }
        return false;
    }

    /**
     * @param userId 用户ID
     * @return 角色ID集合
     * @description: 根据用户ID查询角色ID集合
     */
    @Override
    public List<Integer> getRoleIds(String userId) {
        RBucket<List<Integer>> bucket = redissonClient.getBucket(CacheKey.userRoleCacheKey(userId));
        List<Integer> roleIdList = bucket.get();
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            return roleIdList;
        }
        List<UserRoleInfo> infoList = userRoleInfoMapper.selectUserRoles(userId, RoleStatus.VALID.getKey());
        if (CollectionUtil.isEmpty(infoList)) {
            return new ArrayList<>();
        }
        roleIdList = infoList.stream().map(UserRoleInfo::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }
        bucket.set(roleIdList, CacheKey.USER_ROLE_CACHE_TIMEOUT, TimeUnit.MINUTES);
        return roleIdList;
    }

    /**
     * @param roleIds 角色ID集合
     * @return 菜单ID集合
     * @description: 根据角色ID集合获取菜单ID集合
     */
    @Override
    public List<Integer> getMenuIds(List<Integer> roleIds) {
        List<Integer> allMenuIdList = new ArrayList<>();
        for (Integer roleId : roleIds) {
            RBucket<List<Integer>> bucket = redissonClient.getBucket(CacheKey.roleMenuCacheKey(roleId));
            List<Integer> menuIdList = bucket.get();
            if (CollectionUtil.isEmpty(menuIdList)) {
                List<RoleMenuInfo> roleMenuList = roleMenuInfoMapper.selectByRoleId(roleId);
                if (CollectionUtil.isNotEmpty(roleMenuList)) {
                    menuIdList = roleMenuList.stream().map(RoleMenuInfo::getMenuId).collect(Collectors.toList());
                    bucket.set(menuIdList, CacheKey.ROLE_MENU_CACHE_TIMEOUT, TimeUnit.MINUTES);
                }
            }
            if (CollectionUtil.isNotEmpty(menuIdList)) {
                allMenuIdList.addAll(menuIdList);
            }
        }
        return allMenuIdList;
    }

    /**
     * @param roleId 角色ID
     * @description: 判断角色是否存在
     */
    private void checkRoleExist(Integer roleId) {
        roleInfoMapper.selectById(roleId);
    }

    /**
     * @param userId 用户ID
     * @param dto    角色信息
     * @return 角色对象
     * @description: 构造角色对象
     */
    private RoleInfo buildRoleInfo(String userId, RoleAddDto dto) {
        Integer id = dto.getId();
        RoleInfo byName = roleInfoMapper.selectByName(dto.getName());
        RoleInfo byCode = roleInfoMapper.selectByCode(dto.getNameCode());
        long millis = System.currentTimeMillis();
        // 修改
        if (id != null && id > 0) {
            checkRoleExist(id);
            return RoleInfo.builder()
                    .id(id)
                    .name(dto.getName())
                    .code(dto.getNameCode())
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .updateBy(userId)
                    .updateTime(millis)
                    .build();
        }
        return RoleInfo.builder()
                .name(dto.getName())
                .code(dto.getNameCode())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .isDel(YesOrNo.YES.getKey())
                .isAdmin(YesOrNo.NO.getKey())
                .createBy(userId)
                .createTime(millis)
                .updateBy(userId)
                .updateTime(millis)
                .build();
    }

}
