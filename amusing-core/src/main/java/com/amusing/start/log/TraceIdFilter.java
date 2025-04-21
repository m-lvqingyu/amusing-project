package com.amusing.start.log;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Lv.QingYu
 * @since 2025/2/28
 */
@Slf4j
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String traceId = MDCUtils.getOrGenerateTraceId(request);
            MDCUtils.setTraceId(traceId);
            response.setHeader(MDCUtils.TRACE_ID_KEY, traceId);
        } catch (Exception e) {
            log.error("设置traceId出现异常", e);
        }
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

}
