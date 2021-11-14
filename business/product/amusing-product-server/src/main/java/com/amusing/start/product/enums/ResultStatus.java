package com.amusing.start.product.enums;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
public enum ResultStatus {

    INIT(0, "待处理"),

    SUCCESS(1, "成功"),

    FAIL(2, "失败");


    private final int code;
    private final String msg;

    ResultStatus(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
