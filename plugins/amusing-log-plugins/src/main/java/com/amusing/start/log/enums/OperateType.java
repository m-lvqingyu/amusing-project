package com.amusing.start.log.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/12/28
 */
@Getter
public enum OperateType {

    ADD("1", "新增"),

    UPDATE("2", "更新"),

    DEL("3", "删除"),

    SELECT("4", "查询"),

    DOWNLOAD("5", "导出");

    private String key;

    private String value;

    OperateType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
