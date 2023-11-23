package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.UserBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
public class UserInwardController implements UserClient {

    private final UserBusiness userBusiness;

    @Autowired
    public UserInwardController(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    @Override
    public ApiResult<AccountOutput> accountInfo(String userId) throws CustomException {
        if (StringUtils.isEmpty(userId)) {
            throw new CustomException(CommCode.PARAMETER_ERR);
        }
        return ApiResult.ok(userBusiness.getAccountOutputByUId(userId));
    }

    @Override
    public ApiResult<Integer> accountBalance(String userId) throws CustomException {
        AccountOutput accountOutput = userBusiness.getAccountOutputByUId(userId);
        Integer mainAmount = accountOutput.getMainAmount();
        Integer giveAmount = accountOutput.getGiveAmount();
        Integer frozenAmount = accountOutput.getFrozenAmount();
        return ApiResult.ok(mainAmount + giveAmount - frozenAmount);
    }

    @Override
    public ApiResult<Boolean> payment(PayInput input) throws CustomException {
        if (input == null || StringUtils.isBlank(input.getUserId()) || input.getAmount() == null ||
                input.getAmount() <= CommConstant.ZERO) {
            throw new CustomException(CommCode.PARAMETER_ERR);
        }
        return ApiResult.ok(userBusiness.payment(input.getUserId(), input.getAmount()));
    }

}
