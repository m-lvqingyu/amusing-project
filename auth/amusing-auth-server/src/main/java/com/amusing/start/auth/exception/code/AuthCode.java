package com.amusing.start.auth.exception.code;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 鉴权Code码
 * @date 2021/10/15 22:43
 */
public enum AuthCode implements ResultCode<AuthCode> {

    NOT_PERMISSION(10001, "您的权限不足！"),
    NOT_FOUND_PATH(10002, "您的请求路径不存在！"),

    ERROR_AUTH(10003, "用户名或密码错误"),
    USER_SAVE_ERROR(10004, "用户注册失败，请稍后再试！"),
    USERNAME_PWD_ERROR(10005, "帐号或密码错误，请重新输入"),
    USER_INFORMATION_NOT_EXIST(10006, "用户信息不存在"),
    USER_AMOUNT_UPDATE_ERROR(10007, "用户账户信息更新失败"),
    USER_EXISTS(10008, "此用户名太受欢迎,请更换一个"),
    ;

    private final int code;

    private final String msg;

    AuthCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public AuthCode get() {
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
