package com.amusing.start.log.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @since 2023/11/29
 */
@Getter
public enum OperationUserType {

    ADMIN(1, "后台管理用户"),

    WEB(2, "平台用户"),

    ;

    private Integer key;

    private String value;

    OperationUserType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
