package com.amusing.start.oss.service;

import com.amusing.start.oss.dto.BucketDto;
import com.amusing.start.oss.vo.MinioSignVO;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 文件上传桶操作
 * @since 2024/8/5
 */
public interface MinioBucketService {

    /**
     * 判断Bucket是否存在
     *
     * @param bucketName Bucket名称
     * @return true：存在 false：不存在
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建Bucket
     *
     * @param bucketName Bucket名称
     */
    void createBucket(String bucketName);

    /**
     * 获得Bucket的策略
     *
     * @param bucketName Bucket名称
     * @return 策略
     */
    String getBucketPolicy(String bucketName);

    /**
     * 获得所有Bucket列表
     *
     * @return Bucket列表
     */
    List<BucketDto> getAllBuckets();

    /**
     * 根据bucketName获取其相关信息
     *
     * @param bucketName Bucket名称
     * @return Bucket信息
     */
    BucketDto getBucket(String bucketName);

    /**
     * 根据bucketName删除Bucket
     *
     * @param bucketName Bucket名称
     */
    void removeBucket(String bucketName);

    /**
     * 获取签名
     *
     * @param bucketName Bucket名称
     * @return 签名信息
     */
    MinioSignVO sign(String bucketName);

}
