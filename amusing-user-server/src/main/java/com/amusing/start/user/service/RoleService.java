package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.RoleInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author Lv.QingYu
 * @since 2023/4/12
 */
public interface RoleService {

    /**
     * 角色列表-分页
     *
     * @param name 名称
     * @param code 编码
     * @param size 每页展示数量
     * @param page 页码
     * @return 角色列表
     */
    Page<RoleInfo> page(String name, String code, Integer size, Integer page);

    /**
     * 角色名称是否存在
     *
     * @param id   角色ID
     * @param name 角色名称
     */
    void nameExist(Integer id, String name);

    /**
     * 角色名称是否存在
     *
     * @param name 角色名称
     */
    void nameExist(String name);

    /**
     * 角色编码是否存在
     *
     * @param id   角色ID
     * @param code 角色编码
     */
    void codeExist(Integer id, String code);

    /**
     * 角色编码是否存在
     *
     * @param code 角色编码
     */
    void codeExist(String code);

    /**
     * 角色是否存在
     *
     * @param id 角色ID
     */
    void roleExist(Integer id);

    /**
     * 角色新增
     *
     * @param roleInfo 角色信息
     */
    void update(RoleInfo roleInfo);

    /**
     * 角色修改
     *
     * @param roleInfo 角色信息
     */
    void insert(RoleInfo roleInfo);

    /**
     * 获取管理员角色ID
     *
     * @return 角色ID
     */
    Integer adminRoleId();

}
