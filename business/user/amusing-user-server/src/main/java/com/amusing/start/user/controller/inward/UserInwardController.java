package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.enums.UserCode;
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
    public ApiResult<UserAccountOutput> account(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        UserAccountOutput output = accountService.account(userId);
        if (output == null) {
            return ApiResult.result(UserCode.USER_INFORMATION_NOT_EXIST);
        }
        return ApiResult.ok(output);
    }

    @Override
    public ApiResult<Boolean> mainSettlement(UserSettlementInput input) {
        Optional<UserSettlementInput> optional = checkoutParams(input);
        if (!optional.isPresent()) {
            log.warn("[user]-MainSettlement param err! param:{}", input);
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        Boolean settlement = accountService.mainSettlement(input.getUserId(), input.getAmount());
        return ApiResult.ok(settlement);
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
