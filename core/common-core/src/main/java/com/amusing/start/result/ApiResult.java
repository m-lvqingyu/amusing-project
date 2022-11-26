package com.amusing.start.result;

import com.amusing.start.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lv.QingYu
 * @version 1.0
 * @date 2021/10/15 22:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ApiResult<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    public static <T> ApiResult<T> result(ErrorCode errorCode) {
        return result(errorCode, null);
    }

    public static <T> ApiResult<T> result(ErrorCode errorCode, T data) {
        return new ApiResult<T>(errorCode.getCode(), errorCode.getMsg(), data);
    }

    public static <T> ApiResult<T> ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return result(ErrorCode.SUCCESS, data);
    }

    public boolean isSuccess() {
        return this.code == ErrorCode.SUCCESS.getCode();
    }

}
