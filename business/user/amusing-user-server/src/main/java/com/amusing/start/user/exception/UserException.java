package com.amusing.start.user.exception;

import com.amusing.start.code.CommCode;
import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
public class UserException extends Exception {

    private ResultCode resultCode;

    public UserException(CommCode resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }
}
