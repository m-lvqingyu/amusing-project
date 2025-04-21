package com.amusing.start.oss.config;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Part;
import lombok.SneakyThrows;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Lv.QingYu
 * @description: MinioAsyncClient-分片上传
 * @since 2024/8/5
 */
public class MinioPartUploadClient extends MinioAsyncClient {

    public MinioPartUploadClient(MinioAsyncClient minioAsyncClient) {
        super(minioAsyncClient);
    }


    /**
     * 创建分片上传请求
     *
     * @param bucket           存储桶
     * @param region           区域
     * @param object           对象名
     * @param headers          消息头
     * @param extraQueryParams 额外查询参数
     */
    public String multipartUpload(String bucket,
                                  String region,
                                  String object,
                                  Multimap<String, String> headers,
                                  Multimap<String, String> extraQueryParams) throws Exception {
        return this.createMultipartUploadAsync(
                bucket,
                region,
                object,
                headers,
                extraQueryParams).get().result().uploadId();
    }

    /**
     * 分片上传
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       文件名
     * @param data             文件判断
     * @param length           Length of object data.
     * @param uploadId         Upload ID.
     * @param partNumber       Part number.
     * @param extraHeaders     消息头
     * @param extraQueryParams 额外查询参数
     */
    @SneakyThrows(Exception.class)
    public CompletableFuture<UploadPartResponse> uploadPartAsync(String bucketName,
                                                                 String objectName,
                                                                 Object data,
                                                                 long length,
                                                                 String uploadId,
                                                                 int partNumber,
                                                                 Multimap<String, String> extraHeaders,
                                                                 Multimap<String, String> extraQueryParams) {
        return super.uploadPartAsync(
                bucketName,
                region,
                objectName,
                data,
                length,
                uploadId,
                partNumber,
                extraHeaders,
                extraQueryParams);
    }


    /**
     * 完成分片上传，执行合并文件
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param parts            分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    @Override
    public CompletableFuture<ObjectWriteResponse> completeMultipartUploadAsync(String bucketName,
                                                                               String region,
                                                                               String objectName,
                                                                               String uploadId,
                                                                               Part[] parts,
                                                                               Multimap<String, String> extraHeaders,
                                                                               Multimap<String, String> extraQueryParams)
            throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException {
        return super.completeMultipartUploadAsync(
                bucketName,
                region,
                objectName,
                uploadId,
                parts,
                extraHeaders,
                extraQueryParams);
    }

    /**
     * 查询分片数据
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    @Override
    public CompletableFuture<ListPartsResponse> listPartsAsync(String bucketName,
                                                               String region,
                                                               String objectName,
                                                               Integer maxParts,
                                                               Integer partNumberMarker,
                                                               String uploadId,
                                                               Multimap<String, String> extraHeaders,
                                                               Multimap<String, String> extraQueryParams)
            throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException {
        return super.listPartsAsync(
                bucketName,
                region,
                objectName,
                maxParts,
                partNumberMarker,
                uploadId,
                extraHeaders,
                extraQueryParams);
    }

}
