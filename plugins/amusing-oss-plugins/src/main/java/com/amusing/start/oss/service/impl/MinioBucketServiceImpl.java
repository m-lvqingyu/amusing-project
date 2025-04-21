package com.amusing.start.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.oss.config.FileIdProperties;
import com.amusing.start.oss.dto.BucketDto;
import com.amusing.start.oss.service.MinioBucketService;
import com.amusing.start.oss.vo.MinioSignVO;
import io.minio.*;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lv.QingYu
 * @since 2024/8/5
 */
public class MinioBucketServiceImpl implements MinioBucketService {

    private final MinioClient minioClient;

    private final FileIdProperties fileIdProperties;

    public MinioBucketServiceImpl(MinioClient minioClient,
                                  FileIdProperties fileIdProperties) {
        this.minioClient = minioClient;
        this.fileIdProperties = fileIdProperties;
    }

    private static final int DEF_PLUS_MINUTES = 10;

    private static final String KEY_NAME = "key";

    private static final String X_AMZ_DATE_NAME = "x-amz-date";

    private static final String X_AMZ_SIGNATURE_NAME = "x-amz-signature";

    private static final String X_AMZ_ALGORITHM_NAME = "x-amz-algorithm";

    private static final String X_AMZ_CREDENTIAL_NAME = "x-amz-credential";

    private static final String POLICY_NAME = "policy";

    @Override
    @SneakyThrows(Exception.class)
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public void createBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public String getBucketPolicy(String bucketName) {
        return minioClient.getBucketPolicy(GetBucketPolicyArgs.builder().bucket(bucketName).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public List<BucketDto> getAllBuckets() {
        List<Bucket> buckets = minioClient.listBuckets();
        List<BucketDto> bucketDtoList = new ArrayList<>();
        buckets.forEach(i -> {
            bucketDtoList.add(new BucketDto(i.name()));
        });
        return bucketDtoList;
    }

    @Override
    @SneakyThrows(Exception.class)
    public BucketDto getBucket(String bucketName) {
        List<BucketDto> allBuckets = getAllBuckets();
        for (BucketDto dto : allBuckets) {
            if (dto.getName().equals(bucketName)) {
                return dto;
            }
        }
        return null;
    }

    @Override
    @SneakyThrows(Exception.class)
    public void removeBucket(String bucketName) {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    @SneakyThrows(Exception.class)
    @Override
    public MinioSignVO sign(String bucketName) {
        if (StringUtils.isBlank(bucketName)) {
            throw new CustomException(CommunalCode.PARAMETER_ERR);
        }
        String objectName = IdUtil.getSnowflake(fileIdProperties.getWorkerId(), fileIdProperties.getDatacenterId()).nextIdStr();
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusMinutes(DEF_PLUS_MINUTES));
        policy.addEqualsCondition(KEY_NAME, objectName);
        Map<String, String> map = minioClient.getPresignedPostFormData(policy);
        MinioSignVO vo = buildVo(map);
        vo.setBucket(bucketName).setKey(objectName);
        return vo;
    }

    private MinioSignVO buildVo(Map<String, String> map) {
        MinioSignVO vo = new MinioSignVO();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            if (POLICY_NAME.equalsIgnoreCase(key)) {
                vo.setPolicy(entry.getValue());
            }
            if (X_AMZ_DATE_NAME.equalsIgnoreCase(key)) {
                vo.setXAmzDate(entry.getValue());
            }
            if (X_AMZ_ALGORITHM_NAME.equalsIgnoreCase(key)) {
                vo.setXAmzAlgorithm(entry.getValue());
            }
            if (X_AMZ_SIGNATURE_NAME.equalsIgnoreCase(key)) {
                vo.setXAmzSignature(entry.getValue());
            }
            if (X_AMZ_CREDENTIAL_NAME.equalsIgnoreCase(key)) {
                vo.setXAmzCredential(entry.getValue());
            }
        }
        return vo;
    }
    
}
