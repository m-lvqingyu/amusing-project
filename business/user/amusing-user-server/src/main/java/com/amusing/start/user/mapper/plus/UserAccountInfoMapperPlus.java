package com.amusing.start.user.mapper.plus;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Mapper
public interface UserAccountInfoMapperPlus {

    /**
     * 更新主账户金额
     *
     * @param userId
     * @param originalAmount
     * @param updateAmount
     * @return
     */
    int updateMainAccount(@Param("userId") String userId,
                          @Param("originalAmount") BigDecimal originalAmount,
                          @Param("updateAmount") BigDecimal updateAmount);

    /**
     * 更新副账户金额
     *
     * @param userId
     * @param originalAmount
     * @param updateAmount
     * @return
     */
    int updateGiveAccount(@Param("userId") String userId,
                          @Param("originalAmount") BigDecimal originalAmount,
                          @Param("updateAmount") BigDecimal updateAmount);

}
