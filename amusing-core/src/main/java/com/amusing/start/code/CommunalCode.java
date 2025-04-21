package com.amusing.start.code;

/**
 * @author Lv.QingYu
 * @since 2023/10/8
 */
public enum CommunalCode implements BaseCode<CommunalCode> {

    SUCCESS("200", "成功"),

    UNAUTHORIZED("C01000", "非法访问！"),

    PARAMETER_ERR("C01001", "请求参数不合法"),

    PARAMETER_PARSE_ERR("C01002", "请求参数解析异常"),

    HTTP_MEDIA_TYPE_ERR("C01003", "HTTP Media 类型异常"),

    SERVICE_ERR("C01004", "服务开小差了，请稍后再试~~~"),

    OPERATION_ERR("C01005", "网络异常，请稍后再试"),

    FLOW_ERR("C01006", "请求触发限流"),

    DEGRADE_ERR("C01007", "请求触发熔断"),

    PARAM_FLOW_ERR("C01008", "请求触发热点参数限流"),

    SYSTEM_BLOCK_ERR("C01009", "请求触发系统保护规则"),

    AUTHORITY_ERR("C01010", "请求触发系统保护规则"),

    REQUEST_LIMIT_ERR("C01011", "操作频率过快，请稍后再试"),
    
    REQUEST_LIMIT("C01012", "操作频率过快，请稍后再试"),

    TOKEN_EXPIRES("C01013", "Token已失效，请重新登录");

    private final String key;

    private final String value;

    CommunalCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public CommunalCode get() {
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
