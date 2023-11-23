package com.amusing.start.platform.controller.inward.ding;

import com.amusing.start.client.api.DingTalkMsgClient;
import com.amusing.start.platform.service.ding.IDingTalkMsgService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@RestController
public class DingTalkMsgController implements DingTalkMsgClient {

    private final IDingTalkMsgService dingTalkMsgService;

    @Autowired
    public DingTalkMsgController(IDingTalkMsgService dingTalkMsgService) {
        this.dingTalkMsgService = dingTalkMsgService;
    }

    @Override
    public ApiResult<Long> sendTextMsg(List<String> userIdList, String content) {
        return ApiResult.ok(dingTalkMsgService.sendTextMsg(userIdList, content));
    }

    @Override
    public ApiResult<Long> sendImageMsg(List<String> userIdList, String mediaId) {
        return ApiResult.ok(dingTalkMsgService.sendImageMsg(userIdList, mediaId));
    }

    @Override
    public ApiResult<Long> sendFileMsg(List<String> userIdList, String mediaId) {
        return ApiResult.ok(dingTalkMsgService.sendFileMsg(userIdList, mediaId));
    }

    @Override
    public ApiResult<Long> sendLinkMsg(List<String> userIdList, String title, String text, String messageUrl, String picUrl) {
        return ApiResult.ok(dingTalkMsgService.sendLinkMsg(userIdList, title, text, messageUrl, picUrl));
    }

    @Override
    public ApiResult<Long> sendMarkdownMsg(List<String> userIdList, String title, String text) {
        return ApiResult.ok(dingTalkMsgService.sendMarkdownMsg(userIdList, title, text));
    }

    @Override
    public ApiResult<Long> sendOaMsg(List<String> userIdList, String head, String content) {
        return ApiResult.ok(dingTalkMsgService.sendOaMsg(userIdList, head, content));
    }

    @Override
    public ApiResult<Long> sendCardMsg(List<String> userIdList, String title, String markdown, String singleTitle, String singleUrl) {
        return ApiResult.ok(dingTalkMsgService.sendCardMsg(userIdList, title, markdown, singleTitle, singleUrl));
    }

}
