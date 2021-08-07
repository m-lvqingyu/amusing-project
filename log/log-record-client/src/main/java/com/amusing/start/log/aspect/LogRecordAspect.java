package com.amusing.start.log.aspect;

import com.amusing.start.log.task.LogRecordTask;
import com.amusing.start.log.utils.RequestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;

;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
@Aspect
public class LogRecordAspect {

    private LogRecordTask logRecordTask;

    public LogRecordAspect(LogRecordTask logRecordTask) {
        this.logRecordTask = logRecordTask;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object logRecordHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = RequestUtils.getHttpServletRequest();
        String uri = httpServletRequest.getRequestURI();
        String address = RequestUtils.getIp(httpServletRequest);
        Object[] objArgs = joinPoint.getArgs();
        Object result = joinPoint.proceed();
        logRecordTask.logRecord(address, uri, objArgs, result);
        return result;
    }

}
