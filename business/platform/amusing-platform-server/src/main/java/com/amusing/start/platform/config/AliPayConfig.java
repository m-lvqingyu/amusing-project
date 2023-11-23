package com.amusing.start.platform.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lv.QingYu
 * @description: 支付宝配置信息
 * @since 2023/9/25
 */
@Configuration
public class AliPayConfig {

    private static final String ALI_PAY_SERVER_URL = "https://openapi.alipay.com/gateway.do";

    private static final String DEV_ALI_PAY_SERVER_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    @Value("${ali.pay.app.id}")
    private String aliPayAppId;

    @Value("${ali.pay.private.key}")
    private String aliPayPrivateKey;

    @Value("${ali.pay.public.key}")
    private String aliPayPublicKey;

    @Value("${ali.pay.server.url}")
    private String aliPayServerUrl;

    @Bean(name = "alipayClient")
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(
                aliPayServerUrl,
                aliPayAppId,
                aliPayPrivateKey,
                AlipayConstants.FORMAT_JSON,
                AlipayConstants.CHARSET_UTF8,
                aliPayPublicKey,
                AlipayConstants.SIGN_TYPE_RSA2);
    }
}
