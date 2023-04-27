package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.CommCacheKey;
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
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.mapper.RoleInfoMapper;
import com.amusing.start.user.mapper.RoleMenuInfoMapper;
import com.amusing.start.user.mapper.UserRoleInfoMapper;
import com.amusing.start.user.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
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

    @Override
    public IPage<RoleInfoVo> list(String name, String code, Integer size, Integer page) {
        Page<RoleInfo> of = Page.of(page, size);
        roleInfoMapper.selectByNameOrCode(name, code);

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer add(String userId, RoleAddDto dto) throws CustomException {
        Integer id = dto.getId();
        String name = dto.getName();
        String nameCode = dto.getNameCode();
        // 修改
        if (id != null && id > 0) {
            checkRoleExist(id);
            checkNameAndCodeExist(name, nameCode, id);
            long millis = System.currentTimeMillis();
            RoleInfo roleInfo = RoleInfo.builder()
                    .id(id)
                    .name(name)
                    .code(nameCode)
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .updateBy(userId)
                    .updateTime(millis)
                    .build();
            Integer update = roleInfoMapper.update(roleInfo);
            if (update == null || update <= 0) {
                throw new CustomException(ErrorCode.ROLE_UPDATE_FAIL);
            }
            if (RoleStatus.VALID.getKey() != dto.getStatus()) {
                delRoleMenuMappingCache(id);
            }
            return id;
        }
        // 新增
        checkNameAndCodeExist(name, nameCode, id);
        long millis = System.currentTimeMillis();
        RoleInfo roleInfo = RoleInfo.builder()
                .name(name)
                .code(nameCode)
                .description(dto.getDescription())
                .status(dto.getStatus())
                .isDel(YesOrNo.YES.getKey())
                .isAdmin(YesOrNo.NO.getKey())
                .createBy(userId)
                .createTime(millis)
                .updateBy(userId)
                .updateTime(millis)
                .build();
        Integer insert = roleInfoMapper.insert(roleInfo);
        if (insert == null || insert <= 0) {
            throw new CustomException(ErrorCode.ROLE_UPDATE_FAIL);
        }
        return roleInfo.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean menuBind(String userId, RoleMenuBindDto dto) throws CustomException {
        Integer roleId = dto.getRoleId();
        checkRoleExist(roleId);
        List<Integer> menuIds = dto.getMenuIds();
        if (CollectionUtil.isEmpty(menuIds)) {
            roleMenuInfoMapper.invalidByRoleId(roleId);
            delRoleMenuMappingCache(roleId);
            return true;
        }
        List<MenuInfo> infoList = menuInfoMapper.selectByIds(menuIds);
        if (CollectionUtil.isEmpty(infoList) || infoList.size() != menuIds.size()) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
        roleMenuInfoMapper.invalidByRoleId(roleId);
        long millis = System.currentTimeMillis();
        for (Integer menuId : menuIds) {
            addRoleMenuMapping(roleId, menuId, userId, millis);
        }
        List<String> pathList = infoList.stream().map(MenuInfo::getPath).collect(Collectors.toList());
        addRoleMenuMappingCache(roleId, pathList);
        return true;
    }

    @Override
    public Integer getAdminRoleId() {
        String cacheKey = CacheKey.adminRoleKey();
        RBucket<Integer> bucket = redissonClient.getBucket(cacheKey);
        Integer roleId = bucket.get();
        if (roleId != null && roleId > 0) {
            return roleId;
        }
        roleId = roleInfoMapper.getAdminRoleId();
        if (roleId != null && roleId > 0) {
            bucket.set(roleId);
        }
        return roleId;
    }

    @Override
    public Boolean isAdmin(String userId) throws CustomException {
        List<UserRoleInfo> infoList = userRoleInfoMapper.selectUserRoles(userId, RoleStatus.VALID.getKey());
        if (CollectionUtil.isEmpty(infoList)) {
            return false;
        }
        List<Integer> list = infoList.stream().map(UserRoleInfo::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        Integer adminRoleId = getAdminRoleId();
        if (adminRoleId == null || adminRoleId <= 0) {
            throw new CustomException(ErrorCode.FAIL);
        }
        if (list.contains(adminRoleId)) {
            return true;
        }
        return false;
    }

    /**
     * 根据角色ID，判断角色是否存在
     *
     * @param roleId 角色ID
     * @throws CustomException
     */
    private void checkRoleExist(Integer roleId) throws CustomException {
        RoleInfo byId = roleInfoMapper.selectById(roleId);
        if (byId == null) {
            throw new CustomException(ErrorCode.ROLE_NOT_FUND);
        }
    }

    /**
     * 判断角色名称、编码是否已经存在
     *
     * @param roleName 角色名称
     * @param nameCode 角色编码
     * @param roleId   角色ID
     * @throws CustomException
     */
    private void checkNameAndCodeExist(String roleName, String nameCode, Integer roleId) throws CustomException {
        RoleInfo byName = roleInfoMapper.selectByName(roleName);
        boolean flag = byName != null && (roleId == null || !roleId.equals(byName.getId()));
        if (flag) {
            throw new CustomException(ErrorCode.ROLE_NAME_EXIST);
        }
        RoleInfo byCode = roleInfoMapper.selectByCode(nameCode);
        flag = byCode != null && (roleId == null || !roleId.equals(byCode.getId()));
        if (flag) {
            throw new CustomException(ErrorCode.ROLE_CODE_EXIST);
        }
    }

    /**
     * 新增或修改角色菜单关联关系
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @param userId 用户ID
     * @param millis 时间戳
     * @throws CustomException
     */
    public void addRoleMenuMapping(Integer roleId, Integer menuId, String userId, Long millis) throws CustomException {
        RoleMenuInfo roleMenu = roleMenuInfoMapper.selectByRIdAndMId(roleId, menuId);
        if (roleMenu != null) {
            roleMenu.setStatus(RoleStatus.VALID.getKey());
            roleMenu.setUpdateBy(userId);
            roleMenu.setUpdateTime(millis);
            Integer update = roleMenuInfoMapper.update(roleMenu);
            if (update == null || update <= 0) {
                throw new CustomException(ErrorCode.OPERATE_FAIL);
            }
            return;
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
        if (insert == null || insert <= 0) {
            throw new CustomException(ErrorCode.OPERATE_FAIL);
        }
    }

    /**
     * 根据角色ID，删除角色与路径映射关系缓存
     *
     * @param roleId 角色ID
     */
    private void delRoleMenuMappingCache(Integer roleId) {
        RMap<Integer, List<String>> rMap = getRoleMenuMappingMap();
        rMap.remove(roleId);
    }

    /**
     * 新增角色与路径映射关系缓存
     *
     * @param roleId   角色ID
     * @param pathList 路径集合
     */
    private void addRoleMenuMappingCache(Integer roleId, List<String> pathList) {
        RMap<Integer, List<String>> rMap = getRoleMenuMappingMap();
        rMap.put(roleId, pathList);
    }

    private RMap<Integer, List<String>> getRoleMenuMappingMap() {
        String cacheKey = CommCacheKey.roleMenuMapping();
        return redissonClient.getMap(cacheKey);
    }

}
