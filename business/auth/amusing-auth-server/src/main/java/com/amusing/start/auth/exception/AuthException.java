package com.amusing.start.auth.exception;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
public class AuthException extends Exception {

    private ResultCode resultCode;

    public AuthException(ResultCode resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }

    public ResultCode getAuthCode() {
        return resultCode;
    }
}
