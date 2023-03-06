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
    
}
