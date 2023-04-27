package com.amusing.start.code;

import lombok.Getter;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 公共Code码
 * @date 2021/10/15 22:43
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "非法访问！"),
    FAIL(500, "系统异常"),

    PERMISSION_DENIED(1000, "权限不足"),
    OPERATE_FAIL(1001, "新增或修改操作失败"),


    USER_NOT_FOUND(2000, "用户信息不存在"),
    USER_UPDATE_ERR(2001, "用户信息更新失败"),
    ACCOUNT_INSUFFICIENT(2002, "用户账户金额不足"),
    NAME_EXISTS(2003, "此用户名太受欢迎,请更换一个"),
    USER_INSERT_ERR(2004, "账户信息新增失败"),
    PHONE_EXISTS(2005, "手机号已注册"),
    LOGIN_ERR(2006, "用户名或密码错误"),
    REGISTER_ERR(2007, "用户注册失败，请稍后再试！"),
    IMEI_ERR(2008, "该账户已绑定其他设备。您可在原设备解绑后，继续使用服务"),
    ACCOUNT_FROZEN_ERR(2009, "该账户已被冻结"),
    TOKEN_ERR(2010, "Token已失效，请重新登陆"),
    PAY_ERR(2011, "支付失败"),
    ADMIN_OPERATE_FAIL(2012, "管理员账户不能操作"),

    ORDER_SAVE_FAIL(3001, "订单创建失败"),
    ORDER_NOT_FOUND(3002, "订单信息不存在"),
    PRODUCT_NOT_FOUND(3003, "商品不存在或已被删除"),
    UNABLE_PROVIDE_SERVICE(3004, "服务开小差了，请稍后再试~~~"),
    PRODUCT_NUM_ERROR(3005, "商品库存不足"),
    SHOP_CAR_ERROR(3006, "请选择需要购买的商品"),

    PRODUCT_CREATE_ERR(4001, "商品新增失败"),
    PRODUCT_NAME_EXIST(4002, "商品名称已经存在"),
    PRODUCT_PRICE_ERR(4003, "商品单价需大于零"),
    PRODUCT_DEDUCTION_STOCK(4004, "商品库存更新失败"),
    SHOP_NOT_FOUND(4005, "商铺不存在或已被删除"),
    SHOP_CREATE_ERR(4006, "商铺新增失败"),
    SHOP_NAME_EXIST(4007, "商铺名称已经存在"),

    PARAMETER_ERR(5001, "请求参数不合法"),
    PARAMETER_PARSE_ERR(5002, "请求参数解析异常"),
    HTTP_MEDIA_TYPE_ERR(5003, "HTTP Media 类型异常"),
    SYSTEM_LOGIN_ERR(5005, "系统登录异常"),
    OPERATION_ERR(5006, "网络异常，请稍后再试"),
    FLOW_ERR(5007, "请求触发限流"),
    DEGRADE_ERR(5008, "请求触发熔断"),
    PARAM_FLOW_ERR(5009, "请求触发热点参数限流"),
    SYSTEM_BLOCK_ERR(5010, "请求触发系统保护规则"),
    AUTHORITY_ERR(5011, "请求触发系统保护规则"),
    RESULT_NOT_FOUND(5012, "查询结果不存在"),
    REQUEST_LIMIT(5013, "操作频率过快，请稍后再试"),

    ROLE_NOT_FUND(6001, "角色不存在或已被删除"),
    ROLE_NAME_EXIST(6002, "角色名称已存在"),
    ROLE_CODE_EXIST(6003, "角色编码已存在"),
    ROLE_UPDATE_FAIL(6004, "角色新增或修改失败"),

    MENU_NOT_FOUND(7001, "菜单不存在或已被删除"),


    ;

    private final int code;

    private final String msg;

    ErrorCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

}
