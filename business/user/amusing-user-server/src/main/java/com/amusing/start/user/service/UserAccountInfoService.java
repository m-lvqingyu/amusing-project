package com.amusing.start.user.service;

import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiResult;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public interface UserAccountInfoService {

    /**
     * 根据用户ID获取用户账户详情
     *
     * @param userId
     * @return
     */
    UserAccountOutput account(String userId);

    /**
     * 主账户结算
     *
     * @param userId
     * @param amount
     * @return
     */
    ApiResult userMainSettlement(String userId, String amount);

    /**
     * 副账户结算
     *
     * @param userId
     * @param amount
     * @return
     */
    ApiResult userGiveSettlement(String userId, String amount);

}
