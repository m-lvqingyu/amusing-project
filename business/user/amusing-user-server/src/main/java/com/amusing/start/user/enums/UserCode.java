package com.amusing.start.user.enums;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum UserCode implements ResultCode<UserCode> {

    USER_INFORMATION_NOT_EXIST(300, "用户信息不存在"),
    USER_AMOUNT_UPDATE_ERROR(301, "用户账户信息更新失败"),
    USER_AMOUNT_INSUFFICIENT_ERROR(302, "用户账户金额不足"),
    USER_EXISTS(303, "此用户名太受欢迎,请更换一个"),
    INSERT_ERR(304, "账户信息新增失败");

    private final int code;
    private final String msg;

    UserCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public UserCode get() {
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
