package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserRoleInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface UserRoleInfoMapper {

    List<UserRoleInfo> getUserRoleList(@Param("userId") String userId, @Param("status") Integer status);
}
