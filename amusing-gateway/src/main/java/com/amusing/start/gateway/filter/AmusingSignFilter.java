package com.amusing.start.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.gateway.constant.GatewayConstant;
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
 * @author Lv.QingYu
 * @description: 参数验签过滤器
 * @since 2023/02/09
 */
@Slf4j
@Order(Integer.MIN_VALUE + 1)
@Component
public class AmusingSignFilter implements GlobalFilter {

    /**
     * HttpHeaders中签名key
     */
    private static final String SIGNATURE = "signature";

    /**
     * HttpHeaders中时间戳key
     */
    private static final String TIMESTAMP = "timestamp";

    /**
     * 10秒
     */
    private static final int FIVE_MILLISECONDS = 500000;

    /**
     * @param exchange HTTP请求和响应上下文
     * @param chain    Gateway网关过滤器
     * @return 响应请求
     * @description: 参数验签
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders httpHeaders = exchange.getRequest().getHeaders();
        String timestamp = httpHeaders.getFirst(TIMESTAMP);
        // 请求在5秒内有效
        if (StringUtils.isBlank(timestamp)) {
            log.warn("check timestamp fail! timestamp is null");
            return GatewayUtils.failMono(exchange, CommunalCode.UNAUTHORIZED);
        }
        long time = Long.parseLong(timestamp);
        long currTime = System.currentTimeMillis();
        if (!(time - FIVE_MILLISECONDS <= currTime && currTime <= time + FIVE_MILLISECONDS)) {
            log.warn("check timestamp fail! timestamp out of range. timestamp:{}, currTime:{}", time, currTime);
            return GatewayUtils.failMono(exchange, CommunalCode.UNAUTHORIZED);
        }
        HttpMethod method = exchange.getRequest().getMethod();
        // 非POST、GET、DELETE请求，不用参数验签
        if (!HttpMethod.POST.equals(method) && !HttpMethod.GET.equals(method) && !HttpMethod.DELETE.equals(method)) {
            return chain.filter(exchange);
        }
        String sign = httpHeaders.getFirst(SIGNATURE);
        if (StringUtils.isBlank(sign)) {
            return GatewayUtils.failMono(exchange, CommunalCode.UNAUTHORIZED);
        }
        SortedMap<String, String> encryptMap = new TreeMap<>();
        encryptMap.put(TIMESTAMP, timestamp);
        encryptMap.put(SIGNATURE, sign);
        // POST请求参数验签
        if (HttpMethod.POST.equals(method)) {
            String requestBody = (String) exchange.getAttributes().get(GatewayConstant.REQUEST_BODY_PARAM);
            @SuppressWarnings("unchecked")
            Map<String, String> bodyParamMap = JSONObject.parseObject(requestBody, LinkedHashMap.class, Feature.OrderedField);
            if (CollUtil.isNotEmpty(bodyParamMap)) {
                encryptMap.putAll(bodyParamMap);
            }
            return verifySign(encryptMap) ? chain.filter(exchange) : GatewayUtils.failMono(exchange, CommunalCode.UNAUTHORIZED);
        }
        // GET、DELETE请求参数验签
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
        if (CollUtil.isNotEmpty(queryParams)) {
            for (Map.Entry<String, List<String>> queryMap : queryParams.entrySet()) {
                encryptMap.put(queryMap.getKey(), CollUtil.getFirst(queryMap.getValue()));
            }
        }
        return verifySign(encryptMap) ? chain.filter(exchange) : GatewayUtils.failMono(exchange, CommunalCode.UNAUTHORIZED);
    }

    /**
     * @param params 请求参数
     * @return true:成功 false:失败
     * @description: 参数验签（JSON字符串-MD5，然后进行对比）
     */
    private boolean verifySign(SortedMap<String, String> params) {
        String userSign = params.get(SIGNATURE);
        params.remove(SIGNATURE);
        String paramsJsonStr = JSONObject.toJSONString(params, SerializerFeature.WriteMapNullValue);
        String countSign = DigestUtils.md5DigestAsHex(paramsJsonStr.getBytes()).toUpperCase();
        if (!userSign.equals(countSign)) {
            log.warn("verify sign fail, userSign:{}, paramsSign:{}", userSign, countSign);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
