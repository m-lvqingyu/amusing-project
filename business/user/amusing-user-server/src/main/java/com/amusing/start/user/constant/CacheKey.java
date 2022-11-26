package com.amusing.start.user.constant;

/**
 * Created by lvqingyu on 2022/10/2.
 * Email: qingyu.lv@plusx.cn
 * Copyright(c) 2014 承影互联(科技)有限公司 版权所有
 */
public class CacheKey {

    public static String requestLimitKey(String className, String method, String ip) {
        return "LIMIT:" + className + ":" + method + ":" + ip;
    }

}
