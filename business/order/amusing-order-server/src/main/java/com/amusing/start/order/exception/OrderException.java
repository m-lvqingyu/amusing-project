package com.amusing.start.order.exception;

import com.amusing.start.result.ApiCode;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class OrderException extends Exception {

    private ApiCode apiCode;

    public OrderException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.apiCode = apiCode;
    }
}
