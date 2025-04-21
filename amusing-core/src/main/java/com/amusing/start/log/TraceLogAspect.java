package com.amusing.start.log;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author Lv.QingYu
 * @since 2025/2/28
 */
@Aspect
@RequiredArgsConstructor
@Slf4j
public class TraceLogAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) "
            + "||@annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "||@annotation(org.springframework.web.bind.annotation.GetMapping)"
            + "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)"
            + "||@annotation(org.springframework.web.bind.annotation.PutMapping)")
    private void controllerPointcut() {
    }

    @Around(value = "controllerPointcut()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        RestController restController = target.getClass().getAnnotation(RestController.class);
        Controller controller = target.getClass().getAnnotation(Controller.class);
        if (Objects.isNull(restController) && Objects.isNull(controller)) {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURI = request.getRequestURI();
        log.info("url[ {} ], request  params[{}]", requestURI, joinPoint.getArgs());
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.info("url[ {} ], response [{}]", requestURI, "出现异常");
            throw new RuntimeException(e);
        }
        log.info("url[ {} ], response [{}]", requestURI, JSONUtil.toJsonStr(result));
        return result;
    }
}
