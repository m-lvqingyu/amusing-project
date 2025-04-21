package com.amusing.start.oss.service;

import com.amusing.start.oss.dto.FileDto;
import com.amusing.start.oss.dto.UploadResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 文件上传-MinioService
 * @since 2024/8/2
 */
public interface MinioUploadService {

    /**
     * 判断文件是否存在
     *
     * @param bucketName Bucket名称
     * @param objectName 文件名称
     * @return true:存在 false:不存在
     */
    boolean isObjectExist(String bucketName, String objectName);

    /**
     * 获取文件流
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @return 二进制流
     */
    InputStream getObject(String bucketName, String objectName);

    /**
     * 断点下载
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度
     * @return 二进制流
     */
    InputStream getObject(String bucketName, String objectName, Long offset, Long length);

    /**
     * 获取路径下文件列表
     *
     * @param bucketName 存储桶
     * @param prefix     文件名称
     * @param recursive  是否递归查找，false：模拟文件夹结构查找
     * @return 二进制流
     */
    List<FileDto> listObjects(String bucketName, String prefix, Boolean recursive);

    /**
     * 使用MultipartFile进行文件上传
     *
     * @param bucketName  存储桶
     * @param file        文件名
     * @param objectName  对象名
     * @param contentType 类型
     * @return 可访问文件外链
     */
    UploadResultDto uploadFile(String bucketName, MultipartFile file, String objectName, String contentType);

    /**
     * 图片上传
     *
     * @param bucketName  存储桶
     * @param imageBase64 图片Base64
     * @param imageName   图片名称
     * @return 可访问文件外链
     */
    UploadResultDto uploadImage(String bucketName, String imageBase64, String imageName);

    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     * @return 可访问文件外链
     */
    UploadResultDto uploadFile(String bucketName, String objectName, String fileName);

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     * @return 可访问文件外链
     */
    UploadResultDto uploadFile(String bucketName, String objectName, InputStream inputStream);

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    void createDir(String bucketName, String objectName);

    /**
     * 拷贝文件
     *
     * @param bucketName    存储桶
     * @param objectName    文件名
     * @param srcBucketName 目标存储桶
     * @param srcObjectName 目标文件名
     */
    void copyFile(String bucketName, String objectName, String srcBucketName, String srcObjectName);

    /**
     * 删除文件
     *
     * @param bucketName 存储桶
     * @param objectName 文件名称
     */
    void removeFile(String bucketName, String objectName);

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶
     * @param keys       需要删除的文件列表
     */
    void removeFiles(String bucketName, List<String> keys);

    /**
     * 获取文件外链
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @param expires    过期时间 <=7 秒 （外链有效时间（单位：秒））
     * @return url 外链
     */
    String getPreObjectUrl(String bucketName, String objectName, Integer expires);

    /**
     * 获得文件外链
     *
     * @param bucketName 存储桶
     * @param objectName 文件名
     * @return url 外链
     */
    String getPreObjectUrl(String bucketName, String objectName);

}
