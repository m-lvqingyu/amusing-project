package com.amusing.start.user.enums;

import lombok.Getter;

/**
 * Created by lvqingyu on 2022/10/2.
 */
@Getter
public enum RegisterPreType {

    NAME(1, "姓名"),

    PHONE(2, "手机号");

    private Integer key;

    private String value;

    RegisterPreType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static RegisterPreType match(Integer key) {
        if (key == null) {
            return null;
        }
        for (RegisterPreType s : values()) {
            if (s.key.equals(key)) {
                return s;
            }
        }
        return null;
    }

}
