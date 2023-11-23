package com.amusing.start.gateway.filter;

import com.amusing.start.gateway.constant.GatewayConstant;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author Lv.QingYu
 * @description: POST请求参数预处理过滤器（执行顺序要先于SignFilter过滤器）
 * @see com.amusing.start.gateway.filter.SignFilter
 * @since 2021/09/04
 */
@Slf4j
@Order(Integer.MIN_VALUE)
@Component
public class RequestBodyFilter implements GlobalFilter {

    @Value("${param.sign.check.flag}")
    private Boolean paramSignCheckFlag;

    /**
     * @param exchange HTTP请求和响应上下文
     * @param chain    Gateway网关过滤器链
     * @return 响应结果
     * @description: POST请求参数预处理（只争对JSON格式的请求参数）
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!paramSignCheckFlag) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        if (!HttpMethod.POST.equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        MediaType contentType = request.getHeaders().getContentType();
        boolean flag = contentType == null || !contentType.includes(MediaType.APPLICATION_JSON) ||
                contentType.includes(MediaType.MULTIPART_FORM_DATA);
        if (flag) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(request.getBody())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                }).flatMap(bodyBytes -> {
                    exchange.getAttributes().put(GatewayConstant.REQUEST_BODY_PARAM, new String(bodyBytes, StandardCharsets.UTF_8));
                    return chain.filter(exchange.mutate().request(generateNewRequest(exchange.getRequest(), bodyBytes)).build());
                });
    }

    /**
     * @param request HTTP请求
     * @param bytes   请求参数
     * @return 新的HTTP请求
     * @description: 生成新的HTTP请求对象
     */
    private ServerHttpRequest generateNewRequest(ServerHttpRequest request, byte[] bytes) {
        URI ex = UriComponentsBuilder.fromUri(request.getURI()).build(Boolean.TRUE).toUri();
        ServerHttpRequest newRequest = request.mutate().uri(ex).build();
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer dataBuffer = nettyDataBufferFactory.wrap(bytes);
        Flux<DataBuffer> flux = Flux.just(dataBuffer);
        newRequest = new ServerHttpRequestDecorator(newRequest) {
            @Override
            public Flux<DataBuffer> getBody() {
                return flux;
            }
        };
        return newRequest;
    }

}
