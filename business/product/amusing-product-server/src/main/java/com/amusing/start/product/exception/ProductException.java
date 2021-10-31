package com.amusing.start.product.exception;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public class ProductException extends Exception {

    private ResultCode resultCode;

    public ProductException(ResultCode resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
