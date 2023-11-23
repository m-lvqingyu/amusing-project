package com.amusing.start.platform.controller.inward.ding;

import com.amusing.start.client.api.DingTalkUploadClient;
import com.amusing.start.platform.service.ding.IDingTalkUploadService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@RestController
public class DingTalkUploadController implements DingTalkUploadClient {

    private final IDingTalkUploadService dingTalkUploadService;

    @Autowired
    public DingTalkUploadController(IDingTalkUploadService dingTalkUploadService) {
        this.dingTalkUploadService = dingTalkUploadService;
    }

    @Override
    public ApiResult<String> uploadImage(MultipartFile file) {
        return ApiResult.ok(dingTalkUploadService.uploadImage(file));
    }

    @Override
    public ApiResult<String> uploadVoice(MultipartFile file) {
        return ApiResult.ok(dingTalkUploadService.uploadVoice(file));
    }

    @Override
    public ApiResult<String> uploadFile(MultipartFile file) {
        return ApiResult.ok(dingTalkUploadService.uploadFile(file));
    }

    @Override
    public ApiResult<String> uploadVideo(MultipartFile file) {
        return ApiResult.ok(dingTalkUploadService.uploadVideo(file));
    }

}
