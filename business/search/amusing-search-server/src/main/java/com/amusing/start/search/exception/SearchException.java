package com.amusing.start.search.exception;

import com.amusing.start.code.ResultCode;

/**
 * @author lv.qingyu
 */
public class SearchException extends Exception {

    private final ResultCode<?> resultCode;

    public SearchException(ResultCode<?> resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }

    public ResultCode<?> getResultCode() {
        return resultCode;
    }

}
