package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.response.MenuResponse;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2021/09/21
 */
public interface MenuService {

    /**
     * 根据ID获取菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    MenuInfo get(Integer id);

    /**
     * 判断菜单编码是否存在
     *
     * @param code 菜单编码
     */
    void codeExist(String code);

    /**
     * 判断菜单编码是否存在
     *
     * @param id   菜单ID
     * @param code 菜单编码
     */
    void codeExist(Integer id, String code);

    /**
     * 判断菜单名称是否存在
     *
     * @param name 菜单名称
     */
    void nameExist(String name);

    /**
     * 判断菜单名称是否存在
     *
     * @param id   菜单ID
     * @param name 菜单名称
     */
    void nameExist(Integer id, String name);

    /**
     * 判断父级菜单是否存在
     *
     * @param parentId 父级菜单
     */
    void parentExist(Integer parentId);

    /**
     * 保存菜单
     *
     * @param menuInfo 菜单信息
     */
    void insert(MenuInfo menuInfo);

    /**
     * 更新菜单
     *
     * @param menuInfo 菜单信息
     */
    void update(MenuInfo menuInfo);

    /**
     * 获取子菜单
     *
     * @param parentId 父级菜单ID
     * @return 菜单集合
     */
    List<MenuInfo> child(Integer parentId);

    /**
     * 获取菜单树
     *
     * @param menuIds 菜单ID集合
     * @return 菜单树
     */
    List<MenuResponse> getMenuTree(List<Integer> menuIds);

    /**
     * 获取菜单集合(所有)
     *
     * @return 菜单信息
     */
    List<MenuInfo> getList();

    /**
     * 获取菜单集合
     *
     * @param menuIds 菜单ID集合
     * @return 菜单信息
     */
    List<MenuInfo> getList(List<Integer> menuIds);
    
}
