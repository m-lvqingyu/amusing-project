package com.amusing.start.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.amusing.start.pay.service.ali.h5.*;
import com.amusing.start.pay.service.ali.h5.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@ConditionalOnProperty(prefix = "ali.pay", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties({AliPayH5Properties.class})
public class AliPayConfig {

    @Resource
    private AliPayH5Properties aliPayH5Properties;

    @Bean("alipayClient")
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(aliPayH5Properties.getServerUrl(),
                aliPayH5Properties.getAppId(),
                aliPayH5Properties.getPrivateKey(),
                aliPayH5Properties.getFormat(),
                aliPayH5Properties.getCharset(),
                aliPayH5Properties.getAlipayPublicKey(),
                aliPayH5Properties.getSignType());
    }

    @Bean("aliPayH5InitService")
    public AliPayH5InitService aliPayH5InitService(AlipayClient alipayClient) {
        return new AliPayH5InitServiceImpl(alipayClient);
    }

    @Bean("aliPayH5RefundService")
    public AliPayH5RefundService aliPayH5RefundService(AlipayClient alipayClient) {
        return new AliPayH5RefundServiceImpl(alipayClient);
    }

    @Bean("aliPayH5CloseService")
    public AliPayH5CloseService aliPayH5CloseService(AlipayClient alipayClient) {
        return new AliPayH5CloseServiceImpl(alipayClient);
    }

    @Bean("aliPayH5RefundQueryService")
    public AliPayH5RefundQueryService aliPayH5RefundQueryService(AlipayClient alipayClient) {
        return new AliPayH5RefundQueryServiceImpl(alipayClient);
    }

    @Bean("aliPayH5QueryService")
    public AliPayH5QueryService aliPayH5QueryService(AlipayClient alipayClient) {
        return new AliPayH5QueryServiceImpl(alipayClient);
    }

}
