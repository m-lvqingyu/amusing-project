package com.amusing.start.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lv.qingyu
 */
@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    @Pointcut(value = "@annotation(com.amusing.start.log.LogOutput)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object aroundHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        long statTime = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = httpServletRequest.getRequestURI();
        String address = RequestUtils.getIp(httpServletRequest);
        String objArgs = findParams(joinPoint.getArgs());
        log.info("[request]-start-uri:{}, address:{}, param:{}", uri, address, objArgs);
        Object result = joinPoint.proceed();
        log.info("[request]-end-result:{}, time:{}", result, System.currentTimeMillis() - statTime);
        return result;
    }

    private String findParams(Object[] objArgs) {
        StringBuilder sb = new StringBuilder();
        if (objArgs == null || objArgs.length <= 0) {
            return sb.toString();
        }
        for (Object obj : objArgs) {
            if (obj instanceof MultipartFile ||
                    obj instanceof HttpServletRequest ||
                    obj instanceof HttpServletResponse) {
                continue;
            }
            sb.append(obj.toString());
        }
        return sb.toString();
    }

}
