package com.amusing.start.user.enums;

public enum YesOrNo {

    YES(1, "是"),

    NO(2,"否");

    private int key;

    private String value;

    YesOrNo(int key, String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
