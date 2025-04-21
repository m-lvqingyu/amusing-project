package com.amusing.start.oss.service;

import java.io.InputStream;

/**
 * @author Lv.QingYu
 * @description: 阿里云OSS上传文件
 * @since 2024/8/2
 */
public interface AliUploadService {

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    void createBucket(String bucketName);

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     */
    void deleteBucket(String bucketName);

    /**
     * 创建文件夹
     *
     * @param bucketName 存储桶
     * @param folder     文件夹名称。例如：file/
     */
    void createFolder(String bucketName, String folder);

    /**
     * 根据文件路径+文件名，删除文件
     *
     * @param bucketName 存储桶
     * @param folder     文件夹
     * @param key        文件名
     */
    void deleteFile(String bucketName, String folder, String key);

    /**
     * 文件上传
     *
     * @param bucketName 存储桶
     * @param storePath  存储文件夹
     * @param fileName   文件名
     * @param is         文件InputStream
     */
    String upload(String bucketName, String storePath, String fileName, InputStream is);

    /**
     * 生成外链
     *
     * @param bucketName     存储桶
     * @param path           文件全路径（/file/dog.jpg）
     * @param expirationTime 过期时间（单位：毫秒）
     * @return 外链
     */
    String getUrl(String bucketName, String path, Long expirationTime);

}
