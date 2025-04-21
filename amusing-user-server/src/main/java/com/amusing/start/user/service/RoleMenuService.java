package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.RoleMenuInfo;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/11/15
 */
public interface RoleMenuService {

    /**
     * 根据角色ID，将角色菜单关联关系置为无效
     *
     * @param roleId 角色ID
     */
    void invalid(Integer roleId);

    /**
     * 保存角色菜单关联关系
     *
     * @param info 角色菜单关联关系
     */
    void insert(RoleMenuInfo info);

    /**
     * 根据角色ID，获取关联的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合
     */
    List<Integer> getMenuId(Integer roleId);

    /**
     * 判断菜单是否已关联角色
     *
     * @param menuId 菜单ID
     * @return true：已关联 false：未关联
     */
    Boolean isBind(Integer menuId);

}
