package com.amusing.start.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@Data
@ConfigurationProperties(value = "amusing.oss.ali")
public class AliStorageProperties {

    private String endpoint;

    private String keyId;

    private String keySecret;

}
