package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.gateway.utils.GatewayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Created by 2023/2/9.
 *
 * @author lvqingyu
 */
@Slf4j
@Order(Integer.MIN_VALUE + 1)
@Component
public class SignFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("[SignFilter]-filter start time:{}", System.currentTimeMillis());
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        String sign = httpHeaders.getFirst(GatewayUtils.SIGNATURE);
        if (StringUtils.isBlank(sign)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        String timestamp = httpHeaders.getFirst(GatewayUtils.TIMESTAMP);
        if (StringUtils.isBlank(timestamp)) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        if (System.currentTimeMillis() > Long.parseLong(timestamp) + GatewayUtils.FIVE_MILLISECONDS) {
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }

        SortedMap<String, String> encryptMap = new TreeMap<>();
        encryptMap.put(GatewayUtils.TIMESTAMP, timestamp);
        encryptMap.put(GatewayUtils.SIGNATURE, sign);
        HttpMethod method = exchange.getRequest().getMethod();
        if (HttpMethod.POST.equals(method)) {
            String requestBody = (String) exchange.getAttributes().get(GatewayUtils.REQUEST_BODY_PARAM);
            Map<String, String> bodyParamMap = JSONObject.parseObject(requestBody, LinkedHashMap.class, Feature.OrderedField);
            if (CollUtil.isNotEmpty(bodyParamMap)) {
                encryptMap.putAll(bodyParamMap);
            }
            if (verifySign(encryptMap)) {
                return chain.filter(exchange);
            }
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        if (HttpMethod.GET.equals(method) || HttpMethod.DELETE.equals(method)) {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
            if (CollUtil.isNotEmpty(queryParams)) {
                for (Map.Entry<String, List<String>> queryMap : queryParams.entrySet()) {
                    encryptMap.put(queryMap.getKey(), CollUtil.getFirst(queryMap.getValue()));
                }
            }
            if (verifySign(encryptMap)) {
                return chain.filter(exchange);
            }
            return GatewayUtils.failMono(exchange, ErrorCode.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    private boolean verifySign(SortedMap<String, String> params) {
        String urlSign = params.get(GatewayUtils.SIGNATURE);
        params.remove(GatewayUtils.SIGNATURE);
        String paramsJsonStr = JSONObject.toJSONString(params, SerializerFeature.WriteMapNullValue);
        String paramsSign = DigestUtils.md5DigestAsHex(paramsJsonStr.getBytes()).toUpperCase();
        if (!urlSign.equals(paramsSign)) {
            log.warn("[SignFilter]-verify sign fail, urlSign:{}, paramsSign:{}", urlSign, paramsSign);
            return false;
        }
        return true;
    }


}
