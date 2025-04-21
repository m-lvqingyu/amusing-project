package com.amusing.start.user.enums;

import lombok.Getter;

/**
 * @author Lv.QingYu
 * @description: 注册类型
 * @since 2023/9/12
 */
@Getter
public enum RegisterType {

    PC(1, "Web端注册"),

    APP(2, "APP注册");

    private int key;

    private String value;

    RegisterType(int key, String value) {
        this.key = key;
        this.value = value;
    }

}
