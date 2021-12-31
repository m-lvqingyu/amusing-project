package com.amusing.start.search.exception.handle;

import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author lv.qingyu
 */
@Slf4j
@RestControllerAdvice
public class SearchExceptionHandle {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResult paramExceptionHandle(MethodArgumentNotValidException exception) {
        List<ObjectError> allErrors = exception.getBindingResult().getAllErrors();
        allErrors.forEach(i -> {
            log.error("[search]-params err! msg:{}", i.getDefaultMessage());
        });
        return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
    }

}
