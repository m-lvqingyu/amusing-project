package com.amusing.start.map.exception.handle;

import com.amusing.start.code.CommCode;
import com.amusing.start.code.ResultCode;
import com.amusing.start.map.exception.MapException;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 订单服务统一异常处理
 * @date 2021/10/15 17:50
 */
@Slf4j
@RestControllerAdvice
public class OrderExceptionHandle {

    /**
     * 请求参数校验统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult paramExceptionHandler(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            log.error("[order-service]-[params]-msg:{}", error.getDefaultMessage());
        }
        return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
    }

    /**
     * 订单服务统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MapException.class)
    public ApiResult mapExceptionHandler(MapException exception) {
        ResultCode resultCode = exception.getResultCode();
        return ApiResult.result(resultCode);
    }

}
