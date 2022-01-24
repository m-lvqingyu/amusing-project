package com.amusing.start.auth.exception.handle;

import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import io.seata.rm.datasource.exec.LockWaitTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Slf4j
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
        exception.getBindingResult().getAllErrors().forEach(i -> log.error("[auth]-paramHandler msg:{}", i.getDefaultMessage()));
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
    public ApiResult<AuthCode> authHandler(AuthException exception) {
        return ApiResult.result(exception.getAuthCode());
    }
}

