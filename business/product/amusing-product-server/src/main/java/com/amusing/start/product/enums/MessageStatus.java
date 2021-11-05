package com.amusing.start.product.enums;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 消息状态
 * @date 2021/11/4 22:54
 */
public enum MessageStatus {

    INIT(1, "开始处理"),

    DEDUCTION_FAILED(2, "扣减失败"),

    DEDUCTION_SUCCESS(3, "处理完成");

    private final int code;
    private final String msg;

    MessageStatus(final int code, final String msg) {
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
