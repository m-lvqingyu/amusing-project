package com.amusing.start.order.enums;

/**
 * Create By 2021/10/30
 *
 * @author lvqingyu
 */
public enum YesOrNo {

    YES(1, "是"),

    NO(2, "否"),
    ;

    private int key;
    private String desc;

    YesOrNo(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public static YesOrNo get(String name) {
        return Enum.valueOf(YesOrNo.class, name);
    }
}
