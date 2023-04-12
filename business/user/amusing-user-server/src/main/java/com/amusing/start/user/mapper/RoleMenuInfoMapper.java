package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface RoleMenuInfoMapper {

    List<RoleMenuInfo> getAll();

    List<RoleMenuInfo> getRoleMenuList(@Param("roleIds") List<Integer> roleIds);

    List<RoleMenuInfo> getAllMenuId(@Param("roleId") Integer roleId);

    Integer invalidByRoleId(@Param("roleId") Integer roleId);

    RoleMenuInfo getRoleMenu(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    Integer update(RoleMenuInfo info);

    Integer insert(RoleMenuInfo info);

}
