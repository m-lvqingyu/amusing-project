package com.amusing.start.oss.service.impl;

import com.amusing.start.oss.dto.FileDto;
import com.amusing.start.oss.dto.UploadResultDto;
import com.amusing.start.oss.service.MinioUploadService;
import com.google.common.base.Throwables;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/2
 */
@Slf4j
public class MinioUploadServiceImpl implements MinioUploadService {

    private final MinioClient minioClient;

    public MinioUploadServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    private static final int DEF_PART_SIZE = -1;

    private static final int DEF_OBJ_SIZE = 0;

    private static final int DEF_EXPIRES = 3600;

    @Override
    @SneakyThrows(Exception.class)
    public boolean isObjectExist(String bucketName, String objectName) {
        return minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build()) != null;
    }

    @Override
    @SneakyThrows(Exception.class)
    public InputStream getObject(String bucketName, String objectName) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public InputStream getObject(String bucketName, String objectName, Long offset, Long length) {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName)
                .offset(offset).length(length).build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public List<FileDto> listObjects(String bucketName, String prefix, Boolean recursive) {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(recursive)
                .build());
        List<FileDto> fileDtoList = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item != null) {
                fileDtoList.add(new FileDto(item.objectName(), item.size()));
            }
        }
        return fileDtoList;
    }

    @Override
    @SneakyThrows(Exception.class)
    public UploadResultDto uploadFile(String bucketName,
                                      MultipartFile file,
                                      String objectName,
                                      String contentType) {
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .contentType(contentType)
                .stream(inputStream, inputStream.available(), DEF_PART_SIZE)
                .build());
        return new UploadResultDto(getPreObjectUrl(bucketName, objectName, DEF_EXPIRES));
    }

    @Override
    @SneakyThrows(Exception.class)
    public UploadResultDto uploadImage(String bucketName, String imageBase64, String imageName) {
        if (StringUtils.isBlank(imageBase64)) {
            return null;
        }
        InputStream in = base64ToInputStream(imageBase64);
        return uploadFile(bucketName, imageName, in);
    }

    @Override
    @SneakyThrows(Exception.class)
    public UploadResultDto uploadFile(String bucketName, String objectName, String fileName) {
        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName)
                .build());
        return new UploadResultDto(getPreObjectUrl(bucketName, objectName, DEF_EXPIRES));
    }

    @Override
    @SneakyThrows(Exception.class)
    public UploadResultDto uploadFile(String bucketName, String objectName, InputStream inputStream) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, inputStream.available(), DEF_PART_SIZE)
                .build());
        return new UploadResultDto(getPreObjectUrl(bucketName, objectName, DEF_EXPIRES));
    }

    @Override
    @SneakyThrows(Exception.class)
    public void createDir(String bucketName, String objectName) {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new ByteArrayInputStream(new byte[]{}), DEF_OBJ_SIZE, DEF_PART_SIZE)
                .build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public void copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName) {
        minioClient.copyObject(CopyObjectArgs.builder()
                .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                .bucket(srcBucketName)
                .object(srcObjectName)
                .build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public void removeFile(String bucketName, String objectName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    @Override
    public void removeFiles(String bucketName, List<String> keys) {
        keys.forEach(s -> removeFile(bucketName, s));
    }

    @Override
    @SneakyThrows(Exception.class)
    public String getPreObjectUrl(String bucketName, String objectName, Integer expires) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .expiry(expires)
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    @Override
    @SneakyThrows(Exception.class)
    public String getPreObjectUrl(String bucketName, String objectName) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.GET)
                .build());
    }

    public static InputStream base64ToInputStream(String base64) {
        try {
            byte[] bytes = new BASE64Decoder().decodeBuffer(base64.trim());
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            log.error("[Minio]-base64ToInputStream err! msg:{}", Throwables.getStackTraceAsString(e));
            return null;
        }
    }

}
