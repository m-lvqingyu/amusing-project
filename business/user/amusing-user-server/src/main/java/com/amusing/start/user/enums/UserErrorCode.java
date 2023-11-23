package com.amusing.start.user.enums;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/16
 */
public enum UserErrorCode implements BaseCode<UserErrorCode> {

    USER_NOT_FOUND("U00000001", "用户信息不存在"),

    USER_UPDATE_ERR("U00000002", "用户信息更新失败"),

    ACCOUNT_INSUFFICIENT("U00000003", "用户账户金额不足"),

    NAME_EXISTS("U00000004", "此用户名太受欢迎,请更换一个"),

    USER_INSERT_ERR("U00000005", "账户信息新增失败"),

    PHONE_EXISTS("U00000006", "手机号已注册"),

    LOGIN_USERNAME_OR_PASS_ERR("U00000007", "用户名或密码错误"),

    REGISTER_ERR("U00000008", "用户注册失败，请稍后再试！"),

    IMEI_ERR("U00000009", "该账户已绑定其他设备。您可在原设备解绑后，继续使用服务"),

    ACCOUNT_FROZEN_ERR("U00000010", "该账户已被冻结"),

    TOKEN_ERR("U00000011", "Token已失效，请重新登陆"),

    PAY_ERR("U00000012", "支付失败"),

    ADMIN_OPERATE_FAIL("U00000013", "管理员账户不能操作"),

    PHONE_FORMAT_ERR("U00000014", "手机号码格式不正确"),

    NAME_FORMAT_ERR("U00000015", "用户名格式不正确"),

    MENU_NOT_FOUND("U00000016", "菜单不存在或已被删除"),

    ROLE_NOT_FUND("U00000017", "角色不存在或已被删除"),


    ;

    private String key;

    private String value;

    UserErrorCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public UserErrorCode get() {
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
