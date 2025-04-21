package com.amusing.start.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/7
 */
@Data
@ConfigurationProperties(value = "amusing.ali.h5.pay")
public class AliPayH5Properties {

    private String serverUrl;

    private String appId;

    private String privateKey;

    private String format;

    private String charset;

    private String alipayPublicKey;

    private String signType;
    
}
