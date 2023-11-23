package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.AccountInfo;
import org.apache.ibatis.annotations.Param;

public interface AccountInfoMapper {

    int insert(AccountInfo record);

    int update(AccountInfo record);

    AccountInfo getById(@Param("userId") String userId);

    AccountInfo getByIdLock(@Param("userId") String userId);

    int updateMainAccount(@Param("userId") String userId,
                          @Param("originalAmount") Integer originalAmount,
                          @Param("updateAmount") Integer updateAmount);

}