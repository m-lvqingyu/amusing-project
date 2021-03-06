package com.amusing.start.map.enums;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单Code码
 * @date 2021/10/15 22:43
 */
public enum MapCode implements ResultCode<MapCode> {

    RESULT_NULL(200, "三方服务返回结果为空"),

    RESULT_PROCESSING_ERR(201, "三方服务返回结果解析失败"),

    SERVER_REQUEST_ERR(202, "三方服务请求失败"),

    USER_NOT_FOUND(203, "用户信息不存在"),

    PRODUCT_NOT_FOUND(204, "商品信息不存在"),

    INSUFFICIENT_BALANCE(205, "账户余额不足"),

    UNABLE_PROVIDE_SERVICE(206, "服务开小差了，请稍后再试~~~");

    private final int code;
    private final String msg;

    MapCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public MapCode get() {
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
