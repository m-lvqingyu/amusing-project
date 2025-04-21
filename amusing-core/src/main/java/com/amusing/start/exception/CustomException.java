package com.amusing.start.exception;

import com.amusing.start.code.BaseCode;

/**
 * Created by lvqingyu on 2022/10/4.
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 8748129205334062036L;

    private final BaseCode<?> errorCode;

    public CustomException(BaseCode<?> errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public BaseCode<?> getErrorCode() {
        return this.errorCode;
    }

}
