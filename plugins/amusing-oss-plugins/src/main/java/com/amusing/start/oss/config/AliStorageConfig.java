package com.amusing.start.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.amusing.start.oss.service.AliUploadService;
import com.amusing.start.oss.service.impl.AliUploadServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/6
 */
@ConditionalOnProperty(prefix = "ali.oss", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties({AliStorageProperties.class, FileIdProperties.class})
public class AliStorageConfig {

    @Resource
    private AliStorageProperties aliStorageProperties;

    @Resource
    private FileIdProperties fileIdProperties;

    @Bean("aliStorageClient")
    public OSS aliStorageClient() {
        return new OSSClient(aliStorageProperties.getEndpoint(),
                aliStorageProperties.getKeyId(),
                aliStorageProperties.getKeySecret());
    }

    @Bean("aliUploadService")
    public AliUploadService aliUploadService(@Lazy OSS aliStorageClient) {
        return new AliUploadServiceImpl(aliStorageClient, fileIdProperties);
    }

}
