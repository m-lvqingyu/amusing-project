package com.amusing.start.client.fallback;

import com.amusing.start.client.api.DingTalkUploadClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@Component
public class DingTalkUploadClientFallback implements FallbackFactory<DingTalkUploadClient> {
    @Override
    public DingTalkUploadClient create(Throwable throwable) {
        return new DingTalkUploadClient() {
            @Override
            public ApiResult<String> uploadImage(MultipartFile file) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<String> uploadVoice(MultipartFile file) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<String> uploadFile(MultipartFile file) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<String> uploadVideo(MultipartFile file) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }
        };
    }
}
