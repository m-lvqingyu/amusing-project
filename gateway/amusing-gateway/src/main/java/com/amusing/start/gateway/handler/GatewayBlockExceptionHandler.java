package com.amusing.start.gateway.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 自定义限流异常处理器。自定义SentinelGatewayBlockExceptionHandler会覆盖Sentinel组件默认的限流异常处理器。
 * Sentinel限流会抛出：BlockException。默认限流异常处理器会返回：{"code":429,"message":"Blocked by Sentinel: ParamFlowException"}
 * Create By 2021/9/4
 *
 * @author lvqingyu
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayBlockExceptionHandler extends SentinelGatewayBlockExceptionHandler {

    public GatewayBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
    }

    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String NO_CACHE = "no-cache";
    private static final String ASTERISK = "*";

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        setProps(response);

        ServerHttpRequest request = serverWebExchange.getRequest();
        String path = request.getURI().getPath();

        // 限流
        if (throwable instanceof FlowException) {
            log.error("[gateway]-request is flow, path:{}", path);
            return responseWrite(response, ApiResult.result(ErrorCode.FLOW_ERR));
        }
        // 熔断
        if (throwable instanceof DegradeException) {
            log.error("[gateway]-request is degrade, path:{}", path);
            return responseWrite(response, ApiResult.result(ErrorCode.DEGRADE_ERR));
        }
        // 热点参数限流
        if (throwable instanceof ParamFlowException) {
            log.error("[gateway]-request is paramFlow, path:{}", path);
            return responseWrite(response, ApiResult.result(ErrorCode.PARAM_FLOW_ERR));
        }
        // 系统保护规则
        if (throwable instanceof SystemBlockException) {
            log.error("[gateway]-request is systemBlock, path:{}", path);
            return responseWrite(response, ApiResult.result(ErrorCode.SYSTEM_BLOCK_ERR));
        }
        // 授权规则
        if (throwable instanceof AuthorityException) {
            log.error("[gateway]-request is authority, path:{}", path);
            return responseWrite(response, ApiResult.result(ErrorCode.AUTHORITY_ERR));
        }
        log.error("[gateway]-request err msg:{}", Throwables.getStackTraceAsString(throwable));
        return responseWrite(response, ApiResult.result(ErrorCode.OPERATION_ERR));
    }

    private void setProps(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(ACCESS_CONTROL_ALLOW_ORIGIN, ASTERISK);
        response.getHeaders().set(CACHE_CONTROL, NO_CACHE);
    }

    private Mono<Void> responseWrite(ServerHttpResponse response, ApiResult<?> apiResult) {
        String body = JSONUtil.toJsonStr(apiResult);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}
