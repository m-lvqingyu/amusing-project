package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.client.request.ConsigneeInfoRequest;
import com.amusing.start.client.response.ConsigneeResp;
import com.amusing.start.client.response.UserDetailResp;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.service.AccountService;
import com.amusing.start.user.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
public class InwardUserController implements UserFeignClient {

    private final AccountService accountService;

    private final AuthService authService;

    public InwardUserController(AccountService accountService,
                                AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @Override
    public ApiResult<UserDetailResp> detail(String userId) {
        return null;
    }

    @Override
    public ApiResult<ConsigneeResp> consigneeInfo(ConsigneeInfoRequest request) throws CustomException {
        return null;
    }

    @Override
    public ApiResult<Integer> balance(String userId) {
        AccountInfo accountInfo = accountService.get(userId);
        if (accountInfo == null) {
            return ApiResult.result(UserErrorCode.USER_NOT_FOUND);
        }
        return ApiResult.ok(accountInfo.getMainAmount());
    }

    @Override
    public ApiResult<Boolean> payment(AccountPayRequest request) {
        return ApiResult.ok(accountService.deductions(
                request.getUserId(),
                request.getOrigAmount(),
                request.getAmount()));
    }

    @Override
    public ApiResult<Boolean> auth(String userId, Long version, String uri) {
        return ApiResult.ok(authService.auth(userId, version, uri));
    }

    @Override
    public ApiResult<List<String>> getDataScope(String userId) {
        return ApiResult.ok(Arrays.asList("1", "2", "3"));
    }

}
