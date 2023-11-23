package com.amusing.start.product.enums;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/16
 */
public enum ProductErrorCode implements BaseCode<ProductErrorCode> {

    PRODUCT_CREATE_ERR("P00000001", "商品新增失败"),

    PRODUCT_NAME_EXIST("P00000002", "商品名称已经存在"),

    PRODUCT_PRICE_ERR("P00000003", "商品单价需大于零"),

    PRODUCT_DEDUCTION_STOCK("P00000004", "商品库存更新失败"),

    SHOP_NOT_FOUND("P00000005", "商铺不存在或已被删除"),

    SHOP_CREATE_ERR("P00000006", "商铺新增失败"),

    SHOP_NAME_EXIST("P00000007", "商铺名称已经存在"),

    ;

    private String key;

    private String value;

    ProductErrorCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public ProductErrorCode get() {
        return this;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }
}
