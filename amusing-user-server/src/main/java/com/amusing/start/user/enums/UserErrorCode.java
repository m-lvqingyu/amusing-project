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
    PASSWORD_ERR("U00000007", "用户名或密码错误"),
    REGISTER_ERR("U00000008", "用户注册失败，请稍后再试！"),
    IMEI_ERR("U00000009", "该账户已绑定其他设备。您可在原设备解绑后，继续使用服务"),
    ACCOUNT_FROZEN_ERR("U00000010", "该账户已被冻结"),
    TOKEN_ERR("U00000011", "Token已失效，请重新登陆"),
    PAY_ERR("U00000012", "支付失败"),
    ADMIN_OPERATE_FAIL("U00000013", "管理员账户不能操作"),
    PHONE_FORMAT_ERR("U00000014", "手机号码格式不正确"),
    NAME_FORMAT_ERR("U00000015", "用户名格式不正确"),
    MENU_NOT_FOUND("U00000016", "菜单不存在或已被删除"),
    MENU_EXIST("U00000017", "菜单名称或编码已存在"),
    MENU_STATUS_ERR("U00000018", "菜单状态错误"),
    MENU_DEL_ERR("U00000019", "菜单包含子菜单，请移除子菜单后执行该操作"),
    MENU_DEL_ERR2("U00000020", "菜单与角色关联，请解除关联关系后执行该操作"),
    PW_ERR("U00000021", "密码与确认密码不一致"),
    ROLE_EDIT_ERR("U00000022", "角色与用户关联，请解除关联关系后执行该操作"),
    ROLE_EXIST("R00000001", "角色名称或编码已存在"),
    ROLE_NOT_FUND("R00000002", "角色不存在或已被删除"),
    TOKEN_INVALID("T00000001", "Token已失效，请重新登录"),


    VERIFICATION_CODE_DOES_NOT_EXIST("80000", "验证码不存在"),

    VERIFICATION_CODE_ERROR("80001", "验证码错误"),

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
