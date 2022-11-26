package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public ApiResult<AccountOutput> account(String userId) throws CustomException {
        if (StringUtils.isEmpty(userId)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return ApiResult.ok(accountService.account(userId));
    }

    @Override
    public ApiResult<Boolean> pay(PayInput input) throws CustomException {
        String userId = input.getUserId();
        BigDecimal amount = input.getAmount();
        if (StringUtils.isBlank(userId) || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResult.result(ErrorCode.PARAMETER_ERR);
        }
        return ApiResult.ok(accountService.pay(userId, amount));
    }

}
