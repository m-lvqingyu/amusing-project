package com.amusing.start.platform.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/30
 */
@Getter
public enum AliPayAsyncNotifyStatus {

    INIT(0, "待处理"),

    FINISH(5, "已处理"),

    IGNORE(10, "忽略");

    private int key;

    private String value;

    AliPayAsyncNotifyStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
