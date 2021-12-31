package com.amusing.start.search.enums;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.qingyu
 */
public enum SearchCode implements ResultCode<SearchCode> {

    INDEX_IS_EXIST(400, "索引已经存在"),

    RESPONSE_IS_NULL(401, "服务返回结果为空"),

    SEARCH_SERVER_ERR(402, "检索服务不可用"),

    SHOP_NOT_FOUND(403, "未检索到商铺信息");

    private int code;
    private String value;

    SearchCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public SearchCode get() {
        return this;
    }

    @Override
    public Integer key() {
        return code;
    }

    @Override
    public String value() {
        return value;
    }
}
