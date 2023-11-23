package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuInfoMapper {

    List<RoleMenuInfo> selectAll();

    List<RoleMenuInfo> selectValidAll();

    List<RoleMenuInfo> selectByRoleIds(@Param("roleIds") List<Integer> roleIds);

    List<RoleMenuInfo> selectByRoleId(@Param("roleId") Integer roleId);

    Integer invalidByRoleId(@Param("roleId") Integer roleId);

    RoleMenuInfo selectByRIdAndMId(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    Integer update(RoleMenuInfo info);

    Integer insert(RoleMenuInfo info);

}
