package com.amusing.start.user.service;

import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 角色管理Service
 * @since 2023/4/12
 */
public interface IRoleService {

    /**
     * @param name 角色名称
     * @param code 角色编码
     * @param size 每页条目
     * @param page 页码
     * @return 角色列表
     * @description: 角色列表-分页
     */
    IPage<RoleInfoVo> list(String name, String code, Integer size, Integer page);

    /**
     * @param userId 执行人ID
     * @param dto    角色信息
     * @return 角色ID
     * @description: 角色新增
     */
    Integer add(String userId, RoleAddDto dto);

    /**
     * @param userId 执行人ID
     * @param dto    角色-菜单信息
     * @return true:成功 false:失败
     * @description: 角色菜单绑定
     */
    Boolean menuBind(String userId, RoleMenuBindDto dto);

    /**
     * @return 角色ID
     * @description: 获取管理员角色ID
     */
    Integer getAdminRoleId();

    /**
     * @param userId 用户ID
     * @return true:是 false:不是
     * @description: 判断用户是否是管理员
     */
    Boolean isAdmin(String userId);

    /**
     * @param userId 用户ID
     * @return 角色ID集合
     * @description: 根据用户ID获取角色ID集合
     */
    List<Integer> getRoleIds(String userId);

    /**
     * @param roleIds 角色ID集合
     * @return 菜单ID集合
     * @description: 根据角色ID集合获取菜单ID集合
     */
    List<Integer> getMenuIds(List<Integer> roleIds);

}
