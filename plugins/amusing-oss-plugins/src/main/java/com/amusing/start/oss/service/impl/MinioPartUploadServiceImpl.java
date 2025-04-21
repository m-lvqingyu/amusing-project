package com.amusing.start.oss.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.oss.config.FileIdProperties;
import com.amusing.start.oss.config.MinioPartUploadClient;
import com.amusing.start.oss.service.MinioPartUploadService;
import com.amusing.start.oss.vo.MinioPartUploadInitVO;
import com.google.common.base.Throwables;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2024/8/5
 */
@Slf4j
public class MinioPartUploadServiceImpl implements MinioPartUploadService {

    private final MinioPartUploadClient minioPartUploadClient;

    private final FileIdProperties fileIdProperties;

    public MinioPartUploadServiceImpl(MinioPartUploadClient minioPartUploadClient,
                                      FileIdProperties fileIdProperties) {
        this.minioPartUploadClient = minioPartUploadClient;
        this.fileIdProperties = fileIdProperties;
    }

    private static final int DEF_EXPIRY = 1;

    private static final int DEF_PART_NUMBER_MARKER = 0;

    private static final String CONTENT_TYPE_KEY = "Content-Type";

    private static final String DEF_CONTENT_TYPE = "application/octet-stream";

    private static final String UPLOAD_ID_KEY = "uploadId";

    private static final String PART_NUMBER_KEY = "partNumber";

    private static final String DEF_REGION = null;

    private static final Multimap<String, String> DEF_HEADERS = HashMultimap.create();

    private static final Multimap<String, String> DEF_EXTRA_QUERY_PARAMS = HashMultimap.create();

    public MinioPartUploadInitVO init(String bucketName, Integer partCount, String contentType) {
        // 设置content_type，如果设置不正确，在线预览会出现问题
        if (StringUtils.isBlank(contentType)) {
            contentType = DEF_CONTENT_TYPE;
        }
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put(CONTENT_TYPE_KEY, contentType);
        // 生成文件名
        Long workerId = fileIdProperties.getWorkerId();
        Long datacenterId = fileIdProperties.getDatacenterId();
        String objectName = IdUtil.getSnowflake(workerId, datacenterId).nextIdStr();
        // 获取分片UploadId
        String uploadId;
        try {
            uploadId = minioPartUploadClient.multipartUpload(
                    bucketName,
                    DEF_REGION,
                    objectName,
                    headers,
                    DEF_EXTRA_QUERY_PARAMS);
        } catch (Exception e) {
            log.error("[Minio]-multipartUpload err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        // 生成分片文件预上传链接（注意：通过此链接上传分片文件时，请求类型应为:PUT,数据类型（Content-Type）应该选择/设置：binary）
        List<String> partList = new ArrayList<>();
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put(UPLOAD_ID_KEY, uploadId);
        for (int i = 1; i <= partCount; i++) {
            reqParams.put(PART_NUMBER_KEY, String.valueOf(i));
            String uploadUrl;
            try {
                uploadUrl = minioPartUploadClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(DEF_EXPIRY, TimeUnit.DAYS)
                        .extraQueryParams(reqParams)
                        .build());
            } catch (Exception e) {
                log.error("[Minio]-getPreObjectUrl err! msg:{}", Throwables.getStackTraceAsString(e));
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            partList.add(uploadUrl);
        }
        return new MinioPartUploadInitVO().setUploadId(uploadId).setObjectName(objectName).setUploadUrlList(partList);
    }

    public void merge(String bucketName, String objectName, String uploadId, Integer partCount) {
        CompletableFuture<ListPartsResponse> completableFuture;
        try {
            completableFuture = minioPartUploadClient.listPartsAsync(
                    bucketName,
                    DEF_REGION,
                    objectName,
                    partCount,
                    DEF_PART_NUMBER_MARKER,
                    uploadId,
                    DEF_HEADERS,
                    DEF_EXTRA_QUERY_PARAMS);
        } catch (Exception e) {
            log.error("[Minio]-listPartsAsync err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        try {
            minioPartUploadClient.completeMultipartUploadAsync(
                    bucketName,
                    DEF_REGION,
                    objectName,
                    uploadId,
                    completableFuture.get().result().partList().toArray(new Part[]{}),
                    DEF_HEADERS,
                    DEF_EXTRA_QUERY_PARAMS);
        } catch (Exception e) {
            log.error("[Minio]-completeMultipartUploadAsync err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

}
