package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.enums.AmountType;
import com.amusing.start.user.service.UserAccountInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@RestController
public class UserInwardController implements UserClient {

    private UserAccountInfoService userAccountInfoService;

    @Autowired
    public UserInwardController(UserAccountInfoService userAccountInfoService) {
        this.userAccountInfoService = userAccountInfoService;
    }

    @Override
    public UserAccountOutput account(String userId) {
        return userAccountInfoService.account(userId);
    }

    @Override
    public ApiResult userSettlement(UserSettlementInput input) {
        String userId = input.getUserId();
        String amount = input.getAmount();
        Integer amountType = input.getAmountType();
        if (amountType == null || StringUtils.isEmpty(userId) || StringUtils.isEmpty(amount)) {
            return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION);
        }
        if (amountType == AmountType.MAIN.getKey()) {
            return userAccountInfoService.userMainSettlement(userId, amount);
        }
        if (amountType == AmountType.GIVE.getKey()) {
            return userAccountInfoService.userGiveSettlement(userId, amount);
        }
        return ApiResult.fail(ApiCode.PARAMETER_EXCEPTION);
    }
}
