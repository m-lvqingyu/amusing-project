package com.amusing.start.product.enums;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum ProductCode implements ResultCode<ProductCode> {

    PRODUCT_NOT_FOUND(200, "商品信息不存在");

    private final int code;
    private final String msg;

    ProductCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public ProductCode get() {
        return this;
    }

    @Override
    public Integer key() {
        return code;
    }

    @Override
    public String value() {
        return msg;
    }
}
