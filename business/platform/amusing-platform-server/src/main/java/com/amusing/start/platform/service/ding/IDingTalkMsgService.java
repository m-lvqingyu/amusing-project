package com.amusing.start.platform.service.ding;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 钉钉消息发送API
 * @since 2023/10/17
 */
public interface IDingTalkMsgService {

    /**
     * @param userIdList 接收者ID集合
     * @param content    消息内容
     * @return 钉钉异步发送任务ID
     * @description: 发送文本消息
     */
    Long sendTextMsg(List<String> userIdList, String content);

    /**
     * @param userIdList 接收者ID集合
     * @param mediaId    媒体文件上传后获取的唯一标识
     * @return 钉钉异步发送任务ID
     * @description: 发送图片消息
     */
    Long sendImageMsg(List<String> userIdList, String mediaId);

    /**
     * @param userIdList 接收者ID集合
     * @param mediaId    媒体文件上传后获取的唯一标识
     * @return 钉钉异步发送任务ID
     * @description: 发送文件消息
     */
    Long sendFileMsg(List<String> userIdList, String mediaId);

    /**
     * @param userIdList 接收者ID集合
     * @param title      消息标题
     * @param text       消息描述
     * @param messageUrl 消息链接
     * @param picUrl     消息图标
     * @return 钉钉异步发送任务ID
     * @description: 发送链接消息
     */
    Long sendLinkMsg(List<String> userIdList, String title, String text, String messageUrl, String picUrl);

    /**
     * @param userIdList 接收者ID集合
     * @param title      标题
     * @param text       消息内容
     * @return 钉钉异步发送任务ID
     * @description: 发送Markdown消息
     */
    Long sendMarkdownMsg(List<String> userIdList, String title, String text);

    /**
     * @param userIdList 接收者ID集合
     * @param head       消息头（JSON）
     * @param content    消息体（JSON）
     * @return 钉钉异步发送任务ID
     * @description: 发送Oa消息
     */
    Long sendOaMsg(List<String> userIdList, String head, String content);

    /**
     * @param userIdList  接收者ID集合
     * @param title       透出到会话列表和通知的文案
     * @param markdown    消息内容
     * @param singleTitle 使用整体跳转ActionCard样式时的标题
     * @param singleUrl   跳转地址
     * @return 钉钉异步发送任务ID
     * @description: 发送卡片消息
     */
    Long sendCardMsg(List<String> userIdList, String title, String markdown, String singleTitle, String singleUrl);

}
