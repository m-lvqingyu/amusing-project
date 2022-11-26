package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lvqingyu on 2022/10/2.
 * Email: qingyu.lv@plusx.cn
 * Copyright(c) 2014 承影互联(科技)有限公司 版权所有
 */
@Mapper
public interface UserInfoMapper {

    UserInfo getByName(@Param("name") String name);

    UserInfo getByPhone(@Param("phone") String phone);

    Integer insert(UserInfo userInfo);

    UserInfo getById(@Param("userId") String userId);


}
