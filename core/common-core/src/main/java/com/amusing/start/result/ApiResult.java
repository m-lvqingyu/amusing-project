package com.amusing.start.result;

import com.amusing.start.code.CommCode;
import com.amusing.start.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 统一返回结果封装
 * @date 2021/10/15 22:46
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    public static ApiResult result(ResultCode resultCode) {
        return result(resultCode, null);
    }

    public static ApiResult result(ResultCode resultCode, Object data) {
        return result(resultCode, null, data);
    }

    public static ApiResult result(ResultCode resultCode, String msg, Object data) {
        String message = resultCode.value();
        if (StringUtils.isNotBlank(msg)) {
            message = msg;
        }
        return ApiResult.builder().code(resultCode.key()).msg(message).data(data).build();
    }

    public static ApiResult ok() {
        return ok(null);
    }

    public static ApiResult ok(Object data) {
        return result(CommCode.SUCCESS, data);
    }

    public static ApiResult ok(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return ok(map);
    }

    public static ApiResult fail(ResultCode resultCode) {
        return result(resultCode, null);
    }

    public static ApiResult fail(ResultCode resultCode, Object data) {
        return result(resultCode, data);
    }

}
