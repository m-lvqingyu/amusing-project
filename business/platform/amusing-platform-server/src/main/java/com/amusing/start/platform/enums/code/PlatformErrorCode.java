package com.amusing.start.platform.enums.code;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description: 错误码
 * @since 2023/10/8
 */
public enum PlatformErrorCode implements BaseCode<PlatformErrorCode> {

    // 用户管理
    USER_NOT_FOUND("P010010001", "未找到该用户"),

    INVALID_PHONE("P010010002", "企业中无效的手机号"),

    INVALID_PARAM("P010010003", "无效的参数"),
    ;


    private String key;

    private String value;

    PlatformErrorCode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public PlatformErrorCode get() {
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
