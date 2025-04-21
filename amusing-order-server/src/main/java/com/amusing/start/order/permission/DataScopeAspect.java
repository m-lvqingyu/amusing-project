package com.amusing.start.order.permission;

import com.amusing.start.client.api.UserFeignClient;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2025/3/3
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DataScopeAspect {

    private final UserFeignClient userFeignClient;

    @Pointcut("@annotation(com.amusing.start.order.permission.DataScope)")
    public void injectScope() {
    }

    @Before("injectScope()")
    public void around(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        String userId = request.getHeader(CommConstant.USER_UID);
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommunalCode.TOKEN_EXPIRES);
        }
        ApiResult<List<String>> apiResult = userFeignClient.getDataScope(userId);
        if (apiResult.isSuccess()) {
            throw new CustomException(CommunalCode.UNAUTHORIZED);
        }
        DataScopeHolder.set(apiResult.getData());
    }
}
