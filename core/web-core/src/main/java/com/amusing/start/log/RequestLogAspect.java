package com.amusing.start.log;

import cn.hutool.json.JSONUtil;
import com.amusing.start.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lv.qingyu
 */
@Slf4j
@Aspect
@Component
public class RequestLogAspect {

    @Pointcut("@annotation(com.amusing.start.log.LogOutput)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object aroundHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        long statTime = System.currentTimeMillis();
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest servletRequest = (HttpServletRequest) attributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (servletRequest == null) {
            return joinPoint.proceed();
        }
        String userId = servletRequest.getHeader(AuthConstant.USER_UID);
        String uri = servletRequest.getRequestURI();
        String address = RequestUtils.getIp(servletRequest);
        String objArgs = JSONUtil.toJsonStr(filterArgs(joinPoint.getArgs()));
        log.info("[amusing-request]-start userId:{}, uri:{}, address:{}, param:{}", userId, uri, address, objArgs);
        Object result = joinPoint.proceed();
        log.info("[amusing-request]-end userId:{}, result:{}, timeConsuming:{}ms", userId, result, System.currentTimeMillis() - statTime);
        return result;
    }

    private List<Object> filterArgs(Object[] objects) {
        return Arrays.stream(objects).filter(obj -> !(obj instanceof MultipartFile)
                && !(obj instanceof HttpServletResponse)
                && !(obj instanceof HttpServletRequest)).collect(Collectors.toList());
    }
}
