package com.amusing.start.log;

import cn.hutool.core.lang.UUID;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @author Lv.QingYu
 * @since 2025/2/28
 */
public class MDCUtils {

    public static final String TRACE_ID_KEY = "traceId";

    private static final String NEW_TRACE_ID_KEY_PREFIX = "new_";

    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    public static String getOrGenerateTraceId(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(TRACE_ID_KEY))
                .orElseGet(MDCUtils::generateTraceId);
    }

    public static String getOrGenerateTraceId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return getOrGenerateTraceId(request);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            setMDCContextMap(context);
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            setMDCContextMap(context);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    private static void setMDCContextMap(final Map<String, String> context) {
        if (context == null || context.isEmpty()) {
            MDC.clear();
            setTraceId(NEW_TRACE_ID_KEY_PREFIX + generateTraceId());
        } else {
            MDC.setContextMap(context);
        }
    }

    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
