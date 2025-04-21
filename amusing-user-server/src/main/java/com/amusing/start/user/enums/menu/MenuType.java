package com.amusing.start.user.enums.menu;

import lombok.Getter;

@Getter
public enum MenuType {

    MENU(1, "菜单"),

    BUTTON(5, "按钮");

    private final Integer key;

    private final String value;

    MenuType(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
