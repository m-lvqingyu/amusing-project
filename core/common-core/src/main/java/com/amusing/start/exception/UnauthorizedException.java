package com.amusing.start.exception;

import com.amusing.start.code.ResultCode;

/**
 * @author ：lv.qingyu
 * @date ：2022/4/16 14:46
 */
public class UnauthorizedException extends Exception {

    private final ResultCode<?> resultCode;

    public UnauthorizedException(ResultCode<?> resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;

    }

    public ResultCode<?> getResultCode() {
        return resultCode;
    }

}
