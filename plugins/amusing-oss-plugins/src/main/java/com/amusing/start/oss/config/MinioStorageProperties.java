package com.amusing.start.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "amusing.oss.minio")
public class MinioStorageProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

}
