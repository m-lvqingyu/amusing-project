package com.amusing.start.user.business;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.PageResult;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.pojo.RoleInfo;
import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import com.amusing.start.user.entity.pojo.UserRoleInfo;
import com.amusing.start.user.entity.request.menu.MenuAddRequest;
import com.amusing.start.user.entity.request.menu.MenuEditRequest;
import com.amusing.start.user.entity.request.role.RoleAddRequest;
import com.amusing.start.user.entity.request.role.RoleMenuBindRequest;
import com.amusing.start.user.entity.response.MenuResponse;
import com.amusing.start.user.entity.response.RoleInfoResponse;
import com.amusing.start.user.enums.RoleStatus;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.service.MenuService;
import com.amusing.start.user.service.RoleMenuService;
import com.amusing.start.user.service.RoleService;
import com.amusing.start.user.service.UserRoleService;
import com.amusing.start.user.utils.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @since 2024/11/15
 */
@RequiredArgsConstructor
@Component
public class RoleMenuBusiness {

    private final RoleService roleService;

    private final MenuService menuService;

    private final RoleMenuService roleMenuService;

    private final UserRoleService userRoleService;

    /**
     * 角色列表-分页
     *
     * @param name 角色名称
     * @param code 角色编码
     * @param size 每页数量
     * @param page 页面
     * @return 角色列表
     */
    public PageResult<RoleInfoResponse> rolePage(String name, String code, Integer size, Integer page) {
        Page<RoleInfo> paged = roleService.page(name, code, size, page);
        PageResult<RoleInfoResponse> finalPage = PageUtil.buildDef(paged);
        List<RoleInfo> roleList = Optional.ofNullable(paged.getRecords()).orElse(Collections.emptyList());
        finalPage.setList(roleList.stream().map(this::build).collect(Collectors.toList()));
        return finalPage;
    }

    /**
     * 角色新增
     *
     * @param userId  执行人ID
     * @param request 角色信息
     */
    public void roleAdd(String userId, RoleAddRequest request) {
        // 角色名称是否存在
        roleService.nameExist(request.getName());
        // 角色编码是否存在
        roleService.codeExist(request.getCode());
        Long millis = System.currentTimeMillis();
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(request.getName());
        roleInfo.setCode(request.getCode());
        roleInfo.setDescription(request.getDescription());
        roleInfo.setStatus(request.getStatus());
        roleInfo.setUpdateBy(userId);
        roleInfo.setUpdateTime(millis);
        roleInfo.setIsDel(YesOrNo.YES.getKey());
        roleInfo.setIsAdmin(YesOrNo.NO.getKey());
        roleInfo.setCreateBy(userId);
        roleInfo.setCreateTime(millis);
        roleService.insert(roleInfo);
    }

    /**
     * 角色修改
     *
     * @param userId  执行人ID
     * @param request 角色信息
     */
    public void roleEdit(String userId, RoleAddRequest request) {
        Integer id = request.getId();
        if (id != null && id > 0) {
            throw new CustomException(CommunalCode.PARAMETER_ERR);
        }
        roleService.roleExist(id);
        // 角色名称是否存在
        roleService.nameExist(id, request.getName());
        // 角色编码是否存在
        roleService.codeExist(id, request.getCode());
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setName(request.getName());
        roleInfo.setCode(request.getCode());
        roleInfo.setDescription(request.getDescription());
        roleInfo.setStatus(request.getStatus());
        roleInfo.setUpdateBy(userId);
        roleInfo.setUpdateTime(System.currentTimeMillis());
        if (YesOrNo.YES.getKey() != request.getStatus() && userRoleService.bind(id)) {
            throw new CustomException(UserErrorCode.ROLE_EDIT_ERR);
        }
        roleInfo.setId(id);
        roleService.update(roleInfo);
    }

    /**
     * 菜单新增
     *
     * @param userId  用户ID
     * @param request 菜单信息
     */
    public void menuAdd(String userId, MenuAddRequest request) {
        menuService.codeExist(request.getCode());
        menuService.nameExist(request.getName());
        menuService.parentExist(request.getParentId());
        long millis = System.currentTimeMillis();
        MenuInfo menuInfo = buildMenuInfo(userId, request, millis);
        menuInfo.setCreateBy(userId);
        menuInfo.setCreateTime(millis);
        menuService.insert(menuInfo);
    }

    /**
     * 菜单修改
     *
     * @param userId  用户ID
     * @param request 菜单信息
     */
    public void menuEdit(String userId, MenuEditRequest request) {
        menuService.get(request.getId());
        menuService.codeExist(request.getId(), request.getCode());
        menuService.nameExist(request.getId(), request.getName());
        menuService.parentExist(request.getParentId());
        long millis = System.currentTimeMillis();
        MenuInfo newMenuInfo = buildMenuInfo(userId, request, millis);
        newMenuInfo.setId(request.getId());
        menuService.update(newMenuInfo);
    }

    /**
     * 菜单删除
     *
     * @param userId 用户ID
     * @param id     菜单ID
     */
    public void menuDel(String userId, Integer id) {
        MenuInfo menuInfo = menuService.get(id);
        List<MenuInfo> menuInfoList = menuService.child(id);
        if (CollectionUtils.isNotEmpty(menuInfoList)) {
            throw new CustomException(UserErrorCode.MENU_DEL_ERR);
        }
        if (roleMenuService.isBind(id)) {
            throw new CustomException(UserErrorCode.MENU_DEL_ERR2);
        }
        menuInfo.setIsDel(YesOrNo.NO.getKey()).setUpdateBy(userId).setUpdateTime(System.currentTimeMillis());
        menuService.update(menuInfo);
    }

    /**
     * 根据用户ID，获取其对应的菜单树
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<MenuResponse> getMenuTree(String userId) {
        List<Integer> roleIds = userRoleService.getRoleIdList(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<Integer> menuIds = getMenuIds(roleIds);
        return menuService.getMenuTree(menuIds);
    }

    /**
     * 角色菜单绑定
     *
     * @param userId 执行人ID
     * @param dto    角色菜单关联信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void menuBind(String userId, RoleMenuBindRequest dto) {
        Integer roleId = dto.getRoleId();
        roleService.roleExist(roleId);
        // 该角色所绑定的菜单重置为无效
        roleMenuService.invalid(roleId);
        List<Integer> menuIds = dto.getMenuIds();
        if (CollectionUtil.isEmpty(menuIds)) {
            return;
        }
        // 根据菜单ID查询菜单集合
        List<MenuInfo> infoList = menuService.getList(menuIds);
        if (CollectionUtil.isEmpty(infoList) || infoList.size() != menuIds.size()) {
            throw new CustomException(UserErrorCode.MENU_NOT_FOUND);
        }
        // 菜单与角色进行绑定
        Long millis = System.currentTimeMillis();
        for (Integer menuId : menuIds) {
            roleMenuService.insert(new RoleMenuInfo()
                    .setRoleId(roleId)
                    .setMenuId(menuId)
                    .setStatus(RoleStatus.VALID.getKey())
                    .setIsDel(YesOrNo.YES.getKey())
                    .setCreateBy(userId)
                    .setCreateTime(millis)
                    .setUpdateBy(userId)
                    .setUpdateTime(millis));
        }
    }

    /**
     * 判断用户是否是管理员
     *
     * @param userId 用户ID
     * @return true：是  false：否
     */
    public Boolean isAdmin(String userId) {
        List<Integer> list = userRoleService.getRoleIdList(userId);
        return CollectionUtil.isNotEmpty(list) && list.contains(roleService.adminRoleId());
    }

    /**
     * 根据角色ID，获取其关联的菜单集合
     *
     * @param roleIds 角色ID集合
     * @return 菜单集合
     */
    public List<Integer> getMenuIds(List<Integer> roleIds) {
        return roleIds.stream().map(roleMenuService::getMenuId).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * 用户分配角色
     *
     * @param userId        用户ID
     * @param operateUserId 执行人ID
     * @param roleIdList    角色ID集合
     * @return true：成功 false：失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean bindRole(String userId, String operateUserId, List<Integer> roleIdList) {
        if (isAdmin(userId)) {
            throw new CustomException(CommunalCode.UNAUTHORIZED);
        }
        userRoleService.invalid(userId, operateUserId);
        if (CollectionUtil.isEmpty(roleIdList)) {
            return Boolean.TRUE;
        }
        long currTime = System.currentTimeMillis();
        for (Integer roleId : roleIdList) {
            int insert = userRoleService.insert(new UserRoleInfo().setUserId(userId)
                    .setRoleId(roleId)
                    .setIsDel(YesOrNo.YES.getKey())
                    .setCreateBy(operateUserId)
                    .setCreateTime(currTime)
                    .setUpdateBy(operateUserId)
                    .setUpdateTime(currTime));
            if (insert <= 0) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
        }
        return Boolean.TRUE;
    }

    private RoleInfoResponse build(RoleInfo roleInfo) {
        return new RoleInfoResponse()
                .setId(roleInfo.getId())
                .setName(roleInfo.getName())
                .setCode(roleInfo.getCode())
                .setDescription(roleInfo.getDescription())
                .setStatus(roleInfo.getStatus())
                .setIsAdmin(roleInfo.getIsAdmin())
                .setCreateTime(roleInfo.getCreateTime());
    }

    private MenuInfo buildMenuInfo(String userId, MenuAddRequest request, long millis) {
        return new MenuInfo().setName(request.getName())
                .setCode(request.getCode())
                .setType(request.getType())
                .setPath(request.getPath())
                .setSort(request.getSort())
                .setParentId(request.getParentId())
                .setStatus(request.getStatus())
                .setIsDel(YesOrNo.YES.getKey())
                .setUpdateBy(userId)
                .setUpdateTime(millis);
    }

}
