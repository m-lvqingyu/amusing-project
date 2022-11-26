package com.amusing.start.user.mapper;

import com.amusing.start.user.entity.pojo.AccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author lv.qingyu
 */
@Mapper
public interface AccountInfoMapper {

    Integer insert(AccountInfo record);

    Integer update(AccountInfo record);

    AccountInfo selectById(@Param("userId") String userId);

    Integer updateMainAccount(@Param("userId") String userId,
                              @Param("originalAmount") BigDecimal originalAmount,
                              @Param("updateAmount") BigDecimal updateAmount);

}