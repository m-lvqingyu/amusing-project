package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
@Getter
public enum ProductStatus {

    VALID(1, "有效"),

    INVALID(2, "无效");

    private final int code;

    private final String msg;

    ProductStatus(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

}
