package com.amusing.start.platform.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/26
 */
@Getter
public enum AliTradePagePayStatus {

    CREATE(1, "创建"),

    CLOSE(100, "关闭");

    private int key;

    private String value;

    AliTradePagePayStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

}
