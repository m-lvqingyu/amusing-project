package com.amusing.start.order.enums;

import lombok.Getter;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
@Getter
public enum YesNo {

    YES(1, "是"),

    NO(2, "否"),
    ;

    private final int key;

    private final String value;

    YesNo(int key, String value) {
        this.key = key;
        this.value = value;
    }

}
