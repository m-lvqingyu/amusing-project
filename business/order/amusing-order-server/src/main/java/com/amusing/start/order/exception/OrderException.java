package com.amusing.start.order.exception;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class OrderException extends Exception {

    private ResultCode resultCode;

    public OrderException(ResultCode resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
