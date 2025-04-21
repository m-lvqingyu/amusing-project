package com.amusing.start.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.oss.config.FileIdProperties;
import com.amusing.start.oss.service.AliUploadService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Lv.QingYu
 * @since 2024/8/2
 */
@Slf4j
public class AliUploadServiceImpl implements AliUploadService {

    private final OSS aliStorageClient;

    private final FileIdProperties fileIdProperties;

    public AliUploadServiceImpl(OSS aliStorageClient, FileIdProperties fileIdProperties) {
        this.aliStorageClient = aliStorageClient;
        this.fileIdProperties = fileIdProperties;
    }

    private static final String NO_CACHE_KEY = "Pragma";

    private static final String NO_CACHE_VALUE = "no-cache";

    private static final String DEF_CONTENT_ENCODING = "utf-8";

    private static final String SLASH = "/";

    public void createBucket(String bucketName) {
        if (!aliStorageClient.doesBucketExist(bucketName)) {
            aliStorageClient.createBucket(bucketName);
        }
    }

    public void deleteBucket(String bucketName) {
        aliStorageClient.deleteBucket(bucketName);
    }

    public void createFolder(String bucketName, String folder) {
        if (!aliStorageClient.doesObjectExist(bucketName, folder)) {
            aliStorageClient.putObject(bucketName, folder, new ByteArrayInputStream(new byte[0]));
        }
    }

    public void deleteFile(String bucketName, String folder, String key) {
        aliStorageClient.deleteObject(bucketName, folder + key);
    }

    public String upload(String bucketName, String storePath, String fileName, InputStream is) {
        ObjectMetadata metadata = new ObjectMetadata();
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl(NO_CACHE_VALUE);
        // 指定该Object下设置Header
        metadata.setHeader(NO_CACHE_KEY, NO_CACHE_VALUE);
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding(DEF_CONTENT_ENCODING);
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(getContentType(fileName));
        String ext = FileUtil.extName(fileName);
        Long workerId = fileIdProperties.getWorkerId();
        Long datacenterId = fileIdProperties.getDatacenterId();
        fileName = IdUtil.getSnowflake(workerId, datacenterId).nextIdStr() + StrUtil.DOT + ext;
        try {
            metadata.setContentLength(is.available());
        } catch (Exception e) {
            log.error("[AliOSS]-setContentLength err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        if (!storePath.startsWith(SLASH)) {
            storePath = SLASH + storePath;
        }
        if (!storePath.endsWith(SLASH)) {
            storePath = storePath + SLASH;
        }
        String path = storePath + fileName;
        PutObjectResult putObjectResult = aliStorageClient.putObject(bucketName, path, is, metadata);
        log.info("[AliOSS]-uploadObjectOSS result:{}", JSONUtil.toJsonStr(putObjectResult));
        return path;
    }

    public String getUrl(String bucketName, String path, Long expirationTime) {
        Date expiration = new Date(System.currentTimeMillis() + expirationTime);
        return aliStorageClient.generatePresignedUrl(bucketName, path, expiration).toString();
    }

    public static String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        return "";
    }

}
