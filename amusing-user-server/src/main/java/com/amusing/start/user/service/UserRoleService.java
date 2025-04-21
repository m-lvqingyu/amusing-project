package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.UserRoleInfo;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/11/19
 */
public interface UserRoleService {

    /**
     * 新增用户角色关联关系
     *
     * @param userRoleInfo 用户角色关联关系
     * @return 新增数量
     */
    Integer insert(UserRoleInfo userRoleInfo);

    /**
     * 更新用户角色关联关系
     *
     * @param userRoleInfo 用户角色关联关系
     * @return 修改数量
     */
    Integer update(UserRoleInfo userRoleInfo);

    /**
     * 将用户角色关联关系置为无效
     *
     * @param userId        用户ID
     * @param operateUserId 执行人ID
     */
    void invalid(String userId, String operateUserId);

    /**
     * 根据用户ID，获取其所关联的角色ID集合
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<Integer> getRoleIdList(String userId);

    /**
     * 判断角色是否绑定用户
     *
     * @param roleId 角色ID
     * @return true:已绑定  false:未绑定
     */
    Boolean bind(Integer roleId);

}
