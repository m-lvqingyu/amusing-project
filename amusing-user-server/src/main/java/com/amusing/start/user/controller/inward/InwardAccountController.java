package com.amusing.start.user.controller.inward;

import com.amusing.start.client.api.AccountFeignClient;
import com.amusing.start.client.request.AccountPayRequest;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lv.QingYu
 * @since 2024/3/12
 */

@Slf4j
@RestController
public class InwardAccountController implements AccountFeignClient {

    @Override
    public ApiResult<Integer> amount(String userId) throws CustomException {
        return null;
    }

    @Override
    public ApiResult<?> payment(AccountPayRequest request) throws CustomException {
        return null;
    }
}
