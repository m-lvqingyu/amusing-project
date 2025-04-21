package com.amusing.start.oss.service;

import com.amusing.start.oss.vo.MinioPartUploadInitVO;

/**
 * @author Lv.QingYu
 * @description: 文件上传-分片上传
 * @since 2024/8/5
 */
public interface MinioPartUploadService {

    /**
     * 文件上传-分片-初始化
     *
     * @param bucketName  存储桶
     * @param partCount   分片数量
     * @param contentType 文件类型
     * @return 初始化信息
     */
    MinioPartUploadInitVO init(String bucketName, Integer partCount, String contentType);

    /**
     * 文件上传-分片-合并
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @param uploadId   uploadId
     * @param partCount  分片数量
     */
    void merge(String bucketName, String objectName, String uploadId, Integer partCount);


}
