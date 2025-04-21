package com.amusing.start.gateway.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.gateway.constant.GatewayConstant;
import com.amusing.start.gateway.utils.GatewayUtils;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 自定义限流异常处理器。自定义SentinelGatewayBlockExceptionHandler会覆盖Sentinel组件默认的限流异常处理器。
 * Sentinel限流会抛出：BlockException。默认限流异常处理器会返回：{"code":429,"message":"Blocked by Sentinel: ParamFlowException"}
 * @since 2021/09/04
 */
@Slf4j
public class GatewayBlockExceptionHandler extends SentinelGatewayBlockExceptionHandler {

    public GatewayBlockExceptionHandler(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
        super(viewResolvers, serverCodecConfigurer);
    }

    /**
     * @param serverWebExchange HTTP请求和响应上下文
     * @param throwable         异常信息
     * @return 响应结果
     * @description: 限流异常结果封装
     */
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        // 获取HTTP响应,并设置响应信息
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, GatewayConstant.ASTERISK);
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, GatewayConstant.NO_CACHE);

        ServerHttpRequest request = serverWebExchange.getRequest();
        // 获取HTTP请求 Path 和 IP
        String path = request.getURI().getPath();
        String ip = GatewayUtils.getIp(request);
        // 限流异常
        if (throwable instanceof FlowException) {
            log.error("[gateway]-request is flow, ip:{}, path:{}", ip, path);
            return responseWrite(response, ApiResult.result(CommunalCode.FLOW_ERR));
        }
        // 熔断异常
        if (throwable instanceof DegradeException) {
            log.error("[gateway]-request is degrade, ip:{}, path:{}", ip, path);
            return responseWrite(response, ApiResult.result(CommunalCode.DEGRADE_ERR));
        }
        // 热点参数限流异常
        if (throwable instanceof ParamFlowException) {
            log.error("[gateway]-request is paramFlow, ip:{}, path:{}", ip, path);
            return responseWrite(response, ApiResult.result(CommunalCode.PARAM_FLOW_ERR));
        }
        // 系统保护规则异常
        if (throwable instanceof SystemBlockException) {
            log.error("[gateway]-request is systemBlock, ip:{}, path:{}", ip, path);
            return responseWrite(response, ApiResult.result(CommunalCode.SYSTEM_BLOCK_ERR));
        }
        // 授权规则异常
        if (throwable instanceof AuthorityException) {
            log.error("[gateway]-request is authority, ip:{}, path:{}", ip, path);
            return responseWrite(response, ApiResult.result(CommunalCode.AUTHORITY_ERR));
        }
        log.error("[gateway]-request err! ip:{}, msg:{}", ip, Throwables.getStackTraceAsString(throwable));
        return responseWrite(response, ApiResult.result(CommunalCode.OPERATION_ERR));
    }

    private Mono<Void> responseWrite(ServerHttpResponse response, ApiResult<?> apiResult) {
        String body = JSONUtil.toJsonStr(apiResult);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

}
