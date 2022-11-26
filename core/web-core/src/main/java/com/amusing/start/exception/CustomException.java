package com.amusing.start.exception;

import com.amusing.start.code.ErrorCode;

/**
 * Created by lvqingyu on 2022/10/4.
 */
public class CustomException extends Exception {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
