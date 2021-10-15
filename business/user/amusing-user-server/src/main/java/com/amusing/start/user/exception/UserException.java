package com.amusing.start.user.exception;

import com.amusing.start.code.CommCode;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public class UserException extends Exception {

    private CommCode apiCode;

    public UserException(CommCode apiCode) {
        super(apiCode.getMsg());
        this.apiCode = apiCode;
    }
}
