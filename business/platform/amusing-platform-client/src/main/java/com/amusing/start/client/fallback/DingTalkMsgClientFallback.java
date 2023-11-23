package com.amusing.start.client.fallback;

import com.amusing.start.client.api.DingTalkMsgClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@Component
public class DingTalkMsgClientFallback implements FallbackFactory<DingTalkMsgClient> {

    @Override
    public DingTalkMsgClient create(Throwable throwable) {
        return new DingTalkMsgClient() {
            @Override
            public ApiResult<Long> sendTextMsg(List<String> userIdList, String content) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendImageMsg(List<String> userIdList, String mediaId) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendFileMsg(List<String> userIdList, String mediaId) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendLinkMsg(List<String> userIdList, String title, String text, String messageUrl, String picUrl) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendMarkdownMsg(List<String> userIdList, String title, String text) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendOaMsg(List<String> userIdList, String head, String content) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }

            @Override
            public ApiResult<Long> sendCardMsg(List<String> userIdList, String title, String markdown, String singleTitle, String singleUrl) {
                return ApiResult.result(CommCode.DEGRADE_ERR);
            }
        };
    }

}
