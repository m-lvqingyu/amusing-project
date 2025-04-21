package com.amusing.start.order.permission;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2025/3/3
 */
public class DataScopeHolder {

    public final static ThreadLocal<List<String>> SCOPE_DATA = new ThreadLocal<>();

    public static List<String> get() {
        List<String> scopeList = SCOPE_DATA.get();
        SCOPE_DATA.remove();
        return scopeList;
    }

    public static void set(List<String> data) {
        SCOPE_DATA.set(data);
    }

}
