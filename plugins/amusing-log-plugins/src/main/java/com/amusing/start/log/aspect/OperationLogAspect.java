package com.amusing.start.log.aspect;

import com.amusing.start.constant.CommConstant;
import com.amusing.start.log.annotation.OperationLog;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lv.QingYu
 * @since 2023/12/28
 */
@Slf4j
@Aspect
public class OperationLogAspect {

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return point.proceed();
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        String userId = request.getHeader(CommConstant.USER_UID);
        if (StringUtils.isBlank(userId)) {
            log.warn("[OperationLog]-The request header does not contain userId!");
            return point.proceed();
        }
        // SPel表达式解析
        String value = "";
        String expression = operationLog.expression();
        if (StringUtils.isNotBlank(expression)) {
            try {
                MethodSignature signature = (MethodSignature) point.getSignature();
                EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
                value = SysLogUtils.getValue(context, expression, String.class);
            } catch (Exception e) {
                log.error("[OperationLog]-SPel parsing failed! msg:{}", Throwables.getStackTraceAsString(e));
            }
        }
        log.info("[OperationLog]-userId:{}, type:{}, title:{}, param:{}",
                userId,
                operationLog.type().getKey(),
                operationLog.value(),
                value);
        return point.proceed();
    }

}
