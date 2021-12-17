package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.exception.UserException;
import com.amusing.start.user.service.IUserAccountInfoService;
import com.google.common.base.Throwables;
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

    private final IUserAccountInfoService userAccountInfoService;

    @Autowired
    public UserInwardController(IUserAccountInfoService userAccountInfoService) {
        this.userAccountInfoService = userAccountInfoService;
    }

    @Override
    public UserAccountOutput account(String userId) {
        return userAccountInfoService.account(userId);
    }

    @Override
    public boolean userMainSettlement(UserSettlementInput input) {
        Optional<UserSettlementInput> optional = checkoutParams(input);
        if (!optional.isPresent()) {
            log.warn("[user]-MainSettlement param err! param:{}", input);
            return UserConstant.FALSE;
        }
        try {
            return userAccountInfoService.userMainSettlement(input.getUserId(), input.getAmount());
        } catch (UserException e) {
            log.error("[user]-MainSettlement err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            return UserConstant.FALSE;
        }
    }

    @Override
    public boolean userGiveSettlement(UserSettlementInput input) {
        Optional<UserSettlementInput> optional = checkoutParams(input);
        if (!optional.isPresent()) {
            log.warn("[user]-GiveSettlement param err! param:{}", input);
            return UserConstant.FALSE;
        }
        try {
            return userAccountInfoService.userGiveSettlement(input.getUserId(), input.getAmount());
        } catch (UserException e) {
            log.error("[user]-GiveSettlement err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            return UserConstant.FALSE;
        }
    }

    /**
     * 结算接口参数校验
     *
     * @param input 支付信息
     * @return
     */
    private Optional<UserSettlementInput> checkoutParams(UserSettlementInput input) {
        return Optional.ofNullable(input)
                .filter(i -> StringUtils.isNotEmpty(i.getUserId()) && StringUtils.isNotEmpty(i.getAmount()));
    }

}
