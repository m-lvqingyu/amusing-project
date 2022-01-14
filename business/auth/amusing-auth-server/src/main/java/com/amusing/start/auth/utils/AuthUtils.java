package com.amusing.start.auth.utils;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;

import java.util.Optional;

/**
 * @author lv.qingyu
 */
public class AuthUtils {

    public static void checkApiResultCode(ApiResult<?> apiResult) throws AuthException {
        Optional.ofNullable(apiResult).map(ApiResult::getCode).filter(code -> CommCode.SUCCESS.key().intValue() == code)
                .orElseThrow(() -> new AuthException(AuthCode.USER_SAVE_ERROR));
    }

}
