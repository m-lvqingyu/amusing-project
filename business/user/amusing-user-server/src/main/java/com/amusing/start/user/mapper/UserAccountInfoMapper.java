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
     * @param userId         用户ID
     * @param originalAmount 原主账户金额
     * @param updateAmount   支付金额
     * @return
     */
    Integer updateMainAccount(@Param("userId") String userId,
                              @Param("originalAmount") BigDecimal originalAmount,
                              @Param("updateAmount") BigDecimal updateAmount);

    /**
     * 更新副账户金额
     *
     * @param userId         用户ID
     * @param originalAmount 原副账户金额
     * @param updateAmount   支付金额
     * @return
     */
    Integer updateGiveAccount(@Param("userId") String userId,
                              @Param("originalAmount") BigDecimal originalAmount,
                              @Param("updateAmount") BigDecimal updateAmount);

    /**
     * 保存账户信息
     *
     * @param record 账户信息
     * @return
     */
    Integer insertSelective(UserAccountInfo record);

    /**
     * 更新账户信息
     *
     * @param record 账户信息
     * @return
     */
    Integer updateByPrimaryKeySelective(UserAccountInfo record);

    /**
     * 获取账户详情
     *
     * @param userId 用户ID
     * @return
     */
    UserAccountInfo selectByUserId(@Param("userId") String userId);

    /**
     * 根据ID，判断账户信息是否存在
     *
     * @param userId 用户ID
     * @return 用户自增ID
     */
    String checkAmountIsExist(@Param("userId") String userId);

}