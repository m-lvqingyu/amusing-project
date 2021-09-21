package com.amusing.start.user.exception;

import com.amusing.start.result.ApiCode;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public class UserException extends Exception {

    private ApiCode apiCode;

    public UserException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.apiCode = apiCode;
    }
}
