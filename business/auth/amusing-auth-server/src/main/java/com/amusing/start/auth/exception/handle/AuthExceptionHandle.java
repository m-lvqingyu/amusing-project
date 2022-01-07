package com.amusing.start.auth.exception.handle;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ResultCode;
import com.amusing.start.result.ApiResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@RestControllerAdvice
public class AuthExceptionHandle {

    /**
     * 请求参数校验统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult paramHandler(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        String message = errorList.get(0).getDefaultMessage();
        return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
    }

    /**
     * 鉴权统一异常拦截
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AuthException.class)
    public ApiResult authHandler(AuthException exception) {
        ResultCode authCode = exception.getAuthCode();
        return ApiResult.result(authCode);
    }

}
