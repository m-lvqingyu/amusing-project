package com.amusing.start.exception.handler;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 统一异常处理
 * @date 2021/10/15 17:50
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandle {

    /**
     * 请求参数校验统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult<?> paramExceptionHandler(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            log.error("[user]-[params]-msg:{}", error.getDefaultMessage());
        }
        return ApiResult.result(ErrorCode.PARAMETER_ERR);
    }

    /**
     * 商品服务统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public ApiResult<?> customExceptionHandler(CustomException exception) {
        ErrorCode code = exception.getErrorCode();
        return ApiResult.result(code);
    }

    /**
     * 请求参数校验统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ApiResult<?> constraintViolationExceptionHandler(ConstraintViolationException exception) {
        log.error("[user]-[params]-msg:{}", exception.getMessage());
        return ApiResult.result(ErrorCode.PARAMETER_ERR);
    }

}
