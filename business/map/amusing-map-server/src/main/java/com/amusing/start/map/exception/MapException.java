package com.amusing.start.map.exception;

import com.amusing.start.code.ResultCode;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
public class MapException extends Exception {

    private ResultCode resultCode;

    public MapException(ResultCode resultCode) {
        super(resultCode.value());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
