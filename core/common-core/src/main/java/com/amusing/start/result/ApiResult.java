package com.amusing.start.result;

import com.amusing.start.code.CommCode;
import com.amusing.start.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 统一返回结果封装
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

    public static <T> ApiResult<T> result(ResultCode<?> resultCode) {
        return result(resultCode, null);
    }

    public static <T> ApiResult<T> result(ResultCode<?> resultCode, T data) {
        return new ApiResult<T>(resultCode.key(), resultCode.value(), data);
    }

    public static <T> ApiResult<T> ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return result(CommCode.SUCCESS, data);
    }

    public boolean isSuccess(){
        return this.code == CommCode.SUCCESS.key();
    }

}
