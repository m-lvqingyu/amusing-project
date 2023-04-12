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

    List<RoleInfo> getRoleAll();

    RoleInfo getById(@Param("id") Integer id);

    RoleInfo getByName(@Param("name") String name);

    RoleInfo getByCode(@Param("code") String code);

    Integer update(RoleInfo roleInfo);
    
    Integer insert(RoleInfo roleInfo);


}
