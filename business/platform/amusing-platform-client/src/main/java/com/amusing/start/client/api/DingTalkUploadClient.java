package com.amusing.start.client.api;

import com.amusing.start.client.fallback.DingTalkUploadClientFallback;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lv.QingYu
 * @description: 钉钉文件上传接口
 * @since 2023/10/17
 */
@FeignClient(
        name = "amusing-platform-server",
        fallbackFactory = DingTalkUploadClientFallback.class,
        contextId = "amusing-platform-server-DingTalkUploadClient"
)
public interface DingTalkUploadClient {

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 图片上传
     */
    @PostMapping("platform/in/ding/talk/upload/image")
    ApiResult<String> uploadImage(@RequestParam("file") MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 音频上传
     */
    @PostMapping("platform/in/ding/talk/upload/voice")
    ApiResult<String> uploadVoice(@RequestParam("file") MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 文件上传
     */
    @PostMapping("platform/in/ding/talk/upload/file")
    ApiResult<String> uploadFile(@RequestParam("file") MultipartFile file);

    /**
     * @param file 文件
     * @return 媒体文件上传后获取的唯一标识
     * @description: 视频上传
     */
    @PostMapping("platform/in/ding/talk/upload/video")
    ApiResult<String> uploadVideo(@RequestParam("file") MultipartFile file);

}
