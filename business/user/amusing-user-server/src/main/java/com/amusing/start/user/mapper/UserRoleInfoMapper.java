package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserRoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleInfoMapper {

    List<UserRoleInfo> selectUserRoles(@Param("userId") String userId, @Param("status") Integer status);

    Integer insert(UserRoleInfo userRoleInfo);

    Integer update(UserRoleInfo userRoleInfo);
    
}
