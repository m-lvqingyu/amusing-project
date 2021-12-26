package com.amusing.start.gateway.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 自定义限流异常处理器。自定义SentinelGatewayBlockExceptionHandler会覆盖Sentinel组件默认的限流异常处理器。
 * Sentinel限流会抛出：BlockException。默认限流异常处理器会返回：{"code":429,"message":"Blocked by Sentinel: ParamFlowException"}
 * Create By 2021/9/4
 *
 * @author lvqingyu
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayBlockExceptionHandler extends SentinelGatewayBlockExceptionHandler {

    public GatewayBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body = JSONUtil.toJsonStr(ApiResult.result(CommCode.FREQUENT_OPERATION_EXCEPTION));
        // 请求触发限流
        if (throwable instanceof FlowException) {
            body = JSONUtil.toJsonStr(ApiResult.result(CommCode.FLOW_ERROR));
        }
        // 请求触发熔断
        if (throwable instanceof DegradeException) {
            body = JSONUtil.toJsonStr(ApiResult.result(CommCode.DEGRADE_ERROR));
        }
        //热点参数限流
        if (throwable instanceof ParamFlowException) {
            body = JSONUtil.toJsonStr(ApiResult.result(CommCode.PARAM_FLOW_ERROR));
        }
        // 触发系统保护规则
        if (throwable instanceof SystemBlockException) {
            body = JSONUtil.toJsonStr(ApiResult.result(CommCode.SYSTEM_BLOCK_ERROR));
        }
        // 授权规则不通过
        if (throwable instanceof AuthorityException) {
            body = JSONUtil.toJsonStr(ApiResult.result(CommCode.AUTHORITY_ERROR));
        }
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer));
    }
}
