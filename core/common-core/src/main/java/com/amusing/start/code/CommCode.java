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

    PARAMETER_EXCEPTION(5001, "请求参数不合法"),
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
    ;

    private final int code;
    private final String msg;

    CommCode(final int code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

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
