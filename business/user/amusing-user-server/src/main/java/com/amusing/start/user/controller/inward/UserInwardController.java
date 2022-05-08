package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
public class UserInwardController implements UserClient {

    private final IAccountService accountService;

    @Autowired
    public UserInwardController(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ApiResult<Boolean> init(String userId) {
        Boolean result = accountService.init(userId);
        return ApiResult.ok(result);
    }

    @Override
    public UserAccountOutput account(String userId) {
        return accountService.account(userId);
    }

    @Override
    public Boolean mainSettlement(UserSettlementInput input) {
        Optional<UserSettlementInput> optional = checkoutParams(input);
        if (!optional.isPresent()) {
            log.warn("[user]-MainSettlement param err! param:{}", input);
            return UserConstant.FALSE;
        }
        return accountService.mainSettlement(input.getUserId(), input.getAmount());
    }

    @Override
    public Boolean giveSettlement(UserSettlementInput input) {
        Optional<UserSettlementInput> optional = checkoutParams(input);
        if (!optional.isPresent()) {
            log.warn("[user]-GiveSettlement param err! param:{}", input);
            return UserConstant.FALSE;
        }
        return accountService.giveSettlement(input.getUserId(), input.getAmount());
    }

    /**
     * 结算接口参数校验
     *
     * @param input 支付信息
     * @return
     */
    private Optional<UserSettlementInput> checkoutParams(UserSettlementInput input) {
        return Optional.ofNullable(input)
                .filter(i -> StringUtils.isNotEmpty(i.getUserId()) && i.getAmount() != null);
    }

}
