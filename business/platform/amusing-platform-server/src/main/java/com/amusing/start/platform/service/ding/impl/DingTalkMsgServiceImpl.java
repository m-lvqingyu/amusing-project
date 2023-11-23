package com.amusing.start.platform.service.ding.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.DingTalkConstant;
import com.amusing.start.platform.constant.DingTalkErrorCode;
import com.amusing.start.platform.enums.DingTalkMsgType;
import com.amusing.start.platform.service.ding.IDingTalkMsgService;
import com.amusing.start.platform.service.ding.IDingTalkUserService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.google.common.base.Throwables;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@Service
public class DingTalkMsgServiceImpl implements IDingTalkMsgService {

    @Value("${ding.talk.app.agent.id}")
    private Long dingTalkAgentId;

    private final IDingTalkUserService dingTalkUserService;

    @Autowired
    public DingTalkMsgServiceImpl(IDingTalkUserService dingTalkUserService) {
        this.dingTalkUserService = dingTalkUserService;
    }

    @Override
    public Long sendTextMsg(List<String> userIdList, String content) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildMsg(DingTalkMsgType.TEXT, content);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendImageMsg(List<String> userIdList, String mediaId) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildMsg(DingTalkMsgType.IMAGE, mediaId);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendFileMsg(List<String> userIdList, String mediaId) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildMsg(DingTalkMsgType.FILE, mediaId);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendLinkMsg(List<String> userIdList,
                            String title,
                            String text,
                            String messageUrl,
                            String picUrl) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildLinkMsg(title, text, messageUrl, picUrl);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendMarkdownMsg(List<String> userIdList, String title, String text) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildMarkdownMsg(title, text);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendOaMsg(List<String> userIdList, String head, String content) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildOaMsg(head, content);
        return doSendMsg(userIdList, msg);
    }

    @Override
    public Long sendCardMsg(List<String> userIdList, String title,
                            String markdown,
                            String singleTitle,
                            String singleUrl) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = buildCardMsg(title, markdown, singleTitle, singleUrl);
        return doSendMsg(userIdList, msg);
    }

    private Long doSendMsg(List<String> userIdList, OapiMessageCorpconversationAsyncsendV2Request.Msg msg) {
        DingTalkClient dingTalkClient = new DefaultDingTalkClient(DingTalkConstant.SEND_MSG_PATH);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(dingTalkAgentId);
        // 当没有传递接收者时，全员发送
        if (CollectionUtil.isEmpty(userIdList)) {
            request.setToAllUser(Boolean.TRUE);
        } else {
            request.setUseridList(String.join(CommConstant.COMMA, userIdList));
            request.setToAllUser(Boolean.FALSE);
        }
        request.setMsg(msg);
        String accessToken = dingTalkUserService.getDingTalkAccessToken().getAccessToken();
        OapiMessageCorpconversationAsyncsendV2Response response;
        try {
            response = dingTalkClient.execute(request, accessToken);
        } catch (ApiException e) {
            log.error("[DoSendMsg]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[DoSendMsg]-result:{}", JSONUtil.toJsonStr(response));
        if (DingTalkErrorCode.SUCCESS != response.getErrcode()) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return response.getTaskId();
    }

    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildMsg(DingTalkMsgType msgType, String content) {
        if (DingTalkMsgType.TEXT.name().equalsIgnoreCase(msgType.name())) {
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype(DingTalkMsgType.TEXT.name().toLowerCase());
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent(content);
            return msg;
        }
        if (DingTalkMsgType.IMAGE.name().equalsIgnoreCase(msgType.name())) {
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype(DingTalkMsgType.IMAGE.name().toLowerCase());
            msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
            msg.getImage().setMediaId(content);
            return msg;
        }
        if (DingTalkMsgType.FILE.name().equalsIgnoreCase(msgType.name())) {
            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype(DingTalkMsgType.FILE.name().toLowerCase());
            msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
            msg.getFile().setMediaId(content);
            return msg;
        }
        throw new CustomException(CommCode.PARAMETER_ERR);
    }

    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildLinkMsg(String title,
                                                                           String text,
                                                                           String messageUrl,
                                                                           String picUrl) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype(DingTalkMsgType.LINK.name().toLowerCase());
        msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
        msg.getLink().setTitle(title);
        msg.getLink().setText(text);
        msg.getLink().setMessageUrl(messageUrl);
        msg.getLink().setPicUrl(picUrl);
        return msg;
    }

    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildMarkdownMsg(String title, String text) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype(DingTalkMsgType.MARKDOWN.name().toLowerCase());
        msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
        msg.getMarkdown().setText(text);
        msg.getMarkdown().setTitle(title);
        return msg;
    }

    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildOaMsg(String head, String content) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype(DingTalkMsgType.OA.name().toLowerCase());
        msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
        msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
        msg.getOa().getHead().setText(head);
        msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
        msg.getOa().getBody().setContent(content);
        return msg;
    }

    private OapiMessageCorpconversationAsyncsendV2Request.Msg buildCardMsg(String title,
                                                                           String markdown,
                                                                           String singleTitle,
                                                                           String singleUrl) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype(DingTalkMsgType.ACTION_CARD.name().toLowerCase());
        msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
        msg.getActionCard().setTitle(title);
        msg.getActionCard().setMarkdown(markdown);
        msg.getActionCard().setSingleTitle(singleTitle);
        msg.getActionCard().setSingleUrl(singleUrl);
        return msg;
    }

}
