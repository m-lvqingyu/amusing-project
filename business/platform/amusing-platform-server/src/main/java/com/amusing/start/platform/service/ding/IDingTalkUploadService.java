package com.amusing.start.platform.service.ding;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lv.QingYu
 * @description: 钉钉文件上传API
 * @since 2023/10/17
 */
public interface IDingTalkUploadService {

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 图片上传
     */
    String uploadImage(MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 音频上传
     */
    String uploadVoice(MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 文件上传
     */
    String uploadFile(MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 视频上传
     */
    String uploadVideo(MultipartFile file);

}
