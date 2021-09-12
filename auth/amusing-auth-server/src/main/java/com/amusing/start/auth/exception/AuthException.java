package com.amusing.start.auth.exception;

import com.amusing.start.result.ApiCode;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public class AuthException extends Exception {

    private ApiCode apiCode;

    public AuthException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.apiCode = apiCode;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }
}
