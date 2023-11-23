package com.amusing.start.result;

import com.amusing.start.code.BaseCode;
import com.amusing.start.code.CommCode;
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

    private static final long serialVersionUID = 6591392059181418224L;

    private String code;

    private String message;

    private T data;

    public static <T> ApiResult<T> result(BaseCode<?> errorCode) {
        return result(errorCode, null);
    }

    public static <T> ApiResult<T> result(BaseCode<?> errorCode, T data) {
        return new ApiResult<T>().setCode(errorCode.getKey()).setMessage(errorCode.getValue()).setData(data);
    }

    public static <T> ApiResult<T> result(String code, String msg) {
        return new ApiResult<T>().setCode(code).setMessage(msg);
    }

    public static <T> ApiResult<T> ok() {
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data) {
        return result(CommCode.SUCCESS, data);
    }

    public boolean isSuccess() {
        return this.code.equals(CommCode.SUCCESS.getKey());
    }

}
