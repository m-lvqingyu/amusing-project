package com.amusing.start.oss.config;

import com.amusing.start.oss.service.MinioBucketService;
import com.amusing.start.oss.service.MinioPartUploadService;
import com.amusing.start.oss.service.MinioUploadService;
import com.amusing.start.oss.service.impl.MinioBucketServiceImpl;
import com.amusing.start.oss.service.impl.MinioPartUploadServiceImpl;
import com.amusing.start.oss.service.impl.MinioUploadServiceImpl;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/2
 */
@ConditionalOnProperty(prefix = "minio.oss", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties({MinioStorageProperties.class, FileIdProperties.class})
public class MinioStorageConfig {

    @Resource
    private MinioStorageProperties minioStorageProperties;

    @Resource
    private FileIdProperties fileIdProperties;

    @Bean(name = "minioClient")
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioStorageProperties.getEndpoint())
                .credentials(minioStorageProperties.getAccessKey(), minioStorageProperties.getSecretKey())
                .build();
    }

    @Bean(name = "minioAsyncClient")
    public MinioAsyncClient minioAsyncClient() {
        return new MinioAsyncClient.Builder()
                .endpoint(minioStorageProperties.getEndpoint())
                .credentials(minioStorageProperties.getAccessKey(), minioStorageProperties.getSecretKey())
                .build();
    }

    @Bean(name = "minioPartUploadClient")
    public MinioPartUploadClient minioPartUploadClient(@Lazy MinioAsyncClient minioAsyncClient) {
        return new MinioPartUploadClient(minioAsyncClient);
    }

    @Bean(name = "minioBucketService")
    public MinioBucketService minioBucketService(@Lazy MinioClient minioClient) {
        return new MinioBucketServiceImpl(minioClient, fileIdProperties);
    }

    @Bean(name = "minioUploadService")
    public MinioUploadService minioUploadService(@Lazy MinioClient minioClient) {
        return new MinioUploadServiceImpl(minioClient);
    }

    @Bean(name = "minioPartUploadService")
    public MinioPartUploadService minioPartUploadService(@Lazy MinioPartUploadClient minioPartUploadClient) {
        return new MinioPartUploadServiceImpl(minioPartUploadClient, fileIdProperties);
    }

}
