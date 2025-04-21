package com.amusing.start.user.enums.menu;

import lombok.Getter;

@Getter
public enum MenuStatus {

    EFFECTIVE(5, "有效"),

    INVALID(10, "无效");

    private final Integer key;

    private final String value;

    MenuStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

}
