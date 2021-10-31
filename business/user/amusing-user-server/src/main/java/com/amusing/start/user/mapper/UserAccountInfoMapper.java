package com.amusing.start.user.mapper;

import com.amusing.start.user.pojo.UserAccountInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author lv.qingyu
 */
@Mapper
public interface UserAccountInfoMapper {

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

    int insertSelective(UserAccountInfo record);

    int updateByPrimaryKeySelective(UserAccountInfo record);

    /**
     * 获取账户详情
     *
     * @param userId 用户ID
     * @return
     */
    UserAccountInfo selectByUserId(@Param("userId") String userId);
}