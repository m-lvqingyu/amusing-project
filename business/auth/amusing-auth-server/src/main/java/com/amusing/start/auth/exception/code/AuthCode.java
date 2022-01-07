package com.amusing.start.auth.exception.code;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 鉴权Code码
 * @date 2021/10/15 22:43
 */
public enum AuthCode implements ResultCode<AuthCode> {

    NOT_PERMISSION(400, "您的权限不足！"),
    NOT_FOUND_PATH(401, "您的请求路径不存在！"),

    ERROR_AUTH(402, "用户名或密码错误"),
    USER_SAVE_ERROR(403, "用户注册失败，请稍后再试！"),
    USERNAME_PWD_ERROR(404, "帐号或密码错误，请重新输入"),
    USER_INFORMATION_NOT_EXIST(405, "用户信息不存在"),
    USER_AMOUNT_UPDATE_ERROR(406, "用户账户信息更新失败"),
    USER_PHONE_EXISTS(407, "此用户名或手机号太受欢迎,请更换一个"),
    IMEI_ERROR(408, "该账户已绑定其他设备。您可在原设备解绑后，继续使用服务。")
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
