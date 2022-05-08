package com.amusing.start.product.enums;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum ProductCode implements ResultCode<ProductCode> {

    PRODUCT_NOT_FOUND(200, "商品不存在或已被删除"),
    PRODUCT_CREATE_ERR(201, "商品新增失败"),
    PRODUCT_NAME_EXIST(202, "商品名称已经存在"),
    PRODUCT_PRICE_ERR(203, "商品单价需大于零"),
    PRODUCT_DEDUCTION_STOCK(204, "商品库存更新失败"),

    SHOP_NOT_FOUND(250, "商铺不存在或已被删除"),
    SHOP_CREATE_ERR(251, "商铺新增失败"),
    SHOP_NAME_EXIST(252, "商铺名称已经存在");

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
