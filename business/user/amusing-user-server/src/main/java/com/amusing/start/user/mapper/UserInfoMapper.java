package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Created by 2023/2/15.
 *
 * @author lvqingyu
 */
public interface UserInfoMapper {

    UserInfo getByName(@Param("name") String name);

    UserInfo getByPhone(@Param("phone") String phone);

    Integer insert(UserInfo userInfo);

    UserInfo getById(@Param("userId") String userId);


}
