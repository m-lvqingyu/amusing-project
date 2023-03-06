package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.PayInput;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.service.IAccountService;
import com.amusing.start.user.service.IMenuService;
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

    private final IMenuService menuService;

    @Autowired
    public UserInwardController(IAccountService accountService, IMenuService menuService) {
        this.accountService = accountService;
        this.menuService = menuService;
    }

    @Override
    public AccountOutput account(String userId) throws CustomException {
        if (StringUtils.isEmpty(userId)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return accountService.account(userId);
    }

    @Override
    public Boolean payment(PayInput input) throws CustomException {
        if (input == null || StringUtils.isBlank(input.getUserId()) ||
                input.getAmount() == null || input.getAmount().compareTo(BigDecimal.ZERO) <= UserConstant.ZERO) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return accountService.payment(input.getUserId(), input.getAmount());
    }

    @Override
    public Boolean matchPath(String userId, String path) throws CustomException {
        if (StringUtils.isEmpty(userId) || StringUtils.isBlank(path)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return menuService.matchPath(userId, path);
    }

}
