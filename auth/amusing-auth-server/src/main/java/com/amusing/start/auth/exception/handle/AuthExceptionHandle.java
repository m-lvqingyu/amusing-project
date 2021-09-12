package com.amusing.start.auth.exception.handle;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.result.ApiCode;
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

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult handler(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        String message = errorList.get(0).getDefaultMessage();
        return ApiResult.fail(message);
    }

    @ResponseBody
    @ExceptionHandler(value = AuthException.class)
    public ApiResult handler(AuthException exception) {
        ApiCode apiCode = exception.getApiCode();
        return ApiResult.fail(apiCode);
    }

}
