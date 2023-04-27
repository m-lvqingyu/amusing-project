package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.RoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface RoleInfoMapper {

    List<RoleInfo> selectAll();

    List<RoleInfo> selectValidAll();

    RoleInfo selectById(@Param("id") Integer id);

    RoleInfo selectByName(@Param("name") String name);

    RoleInfo selectByCode(@Param("code") String code);

    List<RoleInfo> selectByNameOrCode(@Param("name") String name, @Param("code") String code);

    Integer update(RoleInfo roleInfo);
    
    Integer insert(RoleInfo roleInfo);

    Integer getAdminRoleId();


}
