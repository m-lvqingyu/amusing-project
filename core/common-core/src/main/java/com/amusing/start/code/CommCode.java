package com.amusing.start.code;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 公共Code码
 * @date 2021/10/15 22:43
 */
public enum CommCode implements ResultCode<CommCode> {

    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "非法访问！"),
    FAIL(500, "系统异常"),

    PARAMETER_EXCEPTION(5001, "请求参数校验异常"),
    PARAMETER_PARSE_EXCEPTION(5002, "请求参数解析异常"),
    HTTP_MEDIA_TYPE_EXCEPTION(5003, "HTTP Media 类型异常"),
    SYSTEM_LOGIN_EXCEPTION(5005, "系统登录异常"),
    FREQUENT_OPERATION_EXCEPTION(5006, "网络异常，请稍后再试"),
    FLOW_ERROR(5007, "请求触发限流"),
    DEGRADE_ERROR(5008, "请求触发熔断"),
    PARAM_FLOW_ERROR(5009, "请求触发热点参数限流"),
    SYSTEM_BLOCK_ERROR(5010, "请求触发系统保护规则"),
    AUTHORITY_ERROR(5011, "请求触发系统保护规则"),
    RESULT_NOT_FOUND(5012, "查询结果不存在"),

    // 用户相关
//    ERROR_AUTH(2000, "用户名或密码错误"),
//    USER_SAVE_ERROR(2001, "用户注册失败，请稍后再试！"),
//    USERNAME_PWD_ERROR(2002, "帐号或密码错误，请重新输入"),
//    USER_INFORMATION_NOT_EXIST(2003, "用户信息不存在"),
//    USER_AMOUNT_UPDATE_ERROR(2004, "用户账户信息更新失败"),
//    USER_AMOUNT_INSUFFICIENT_ERROR(2005, "用户账户金额不足"),
//    USER_EXISTS(2006, "此用户名太受欢迎,请更换一个"),
//
//
//    // 库存相关
//    PRODUCT_INVENTORY_NOT_EXIST(3000, "商品库存信息不存在"),
//    PRODUCT_INVENTORY_INSUFFICIENT(3001, "商品库存不足"),
//    PRODUCT_INVENTORY_UPDATE_ERROR(3002, "商品库存更新失败"),
    ;

    private final int code;
    private final String msg;

    CommCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

//    public static CommCode getApiCode(int code) {
//        CommCode[] ecs = CommCode.values();
//        for (CommCode ec : ecs) {
//            if (ec.getCode() == code) {
//                return ec;
//            }
//        }
//        return SUCCESS;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }

    @Override
    public CommCode get() {
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
