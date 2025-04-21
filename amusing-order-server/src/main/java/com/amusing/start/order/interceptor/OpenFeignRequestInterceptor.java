package com.amusing.start.order.interceptor;

import com.amusing.start.log.MDCUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @since 2025/3/4
 */
@Component
public class OpenFeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = MDCUtils.getTraceId();
        requestTemplate.header(MDCUtils.TRACE_ID_KEY, traceId);
    }

}
