package com.amusing.start.client.api;

import com.amusing.start.client.fallback.DingTalkMsgClientFallback;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 钉钉消息发送接口
 * @since 2023/10/17
 */
@FeignClient(
        name = "amusing-platform-server",
        fallbackFactory = DingTalkMsgClientFallback.class,
        contextId = "amusing-platform-server-DingTalkMsgClient"
)
public interface DingTalkMsgClient {

    /**
     * @param userIdList 接收者ID集合
     * @param content    消息内容
     * @return 钉钉异步发送任务ID
     * @description: 发送文本消息
     */
    @PostMapping("platform/in/ding/talk/send/text/msg")
    ApiResult<Long> sendTextMsg(@RequestParam("userIdList") List<String> userIdList,
                                @RequestParam("content") String content);

    /**
     * @param userIdList 接收者ID集合
     * @param mediaId    媒体文件上传后获取的唯一标识
     * @return 钉钉异步发送任务ID
     * @description: 发送图片消息
     */
    @PostMapping("platform/in/ding/talk/send/image/msg")
    ApiResult<Long> sendImageMsg(@RequestParam("userIdList") List<String> userIdList,
                                 @RequestParam("mediaId") String mediaId);

    /**
     * @param userIdList 接收者ID集合
     * @param mediaId    媒体文件上传后获取的唯一标识
     * @return 钉钉异步发送任务ID
     * @description: 发送文件消息
     */
    @PostMapping("platform/in/ding/talk/send/file/msg")
    ApiResult<Long> sendFileMsg(@RequestParam("userIdList") List<String> userIdList,
                                @RequestParam("mediaId") String mediaId);

    /**
     * @param userIdList 接收者ID集合
     * @param title      消息标题
     * @param text       消息描述
     * @param messageUrl 消息链接
     * @param picUrl     消息图标
     * @return 钉钉异步发送任务ID
     * @description: 发送链接消息
     */
    @PostMapping("platform/in/ding/talk/send/link/msg")
    ApiResult<Long> sendLinkMsg(@RequestParam("userIdList") List<String> userIdList,
                                @RequestParam("title") String title,
                                @RequestParam("text") String text,
                                @RequestParam("messageUrl") String messageUrl,
                                @RequestParam("picUrl") String picUrl);

    /**
     * @param userIdList 接收者ID集合
     * @param title      标题
     * @param text       消息内容
     * @return 钉钉异步发送任务ID
     * @description: 发送Markdown消息
     */
    @PostMapping("platform/in/ding/talk/send/markdown/msg")
    ApiResult<Long> sendMarkdownMsg(@RequestParam("userIdList") List<String> userIdList,
                                    @RequestParam("title") String title,
                                    @RequestParam("text") String text);

    /**
     * @param userIdList 接收者ID集合
     * @param head       消息头（JSON）
     * @param content    消息体（JSON）
     * @return 钉钉异步发送任务ID
     * @description: 发送Oa消息
     */
    @PostMapping("platform/in/ding/talk/send/oa/msg")
    ApiResult<Long> sendOaMsg(@RequestParam("userIdList") List<String> userIdList,
                              @RequestParam("head") String head,
                              @RequestParam("content") String content);

    /**
     * @param userIdList  接收者ID集合
     * @param title       透出到会话列表和通知的文案
     * @param markdown    消息内容
     * @param singleTitle 使用整体跳转ActionCard样式时的标题
     * @param singleUrl   跳转地址
     * @return 钉钉异步发送任务ID
     * @description: 发送卡片消息
     */
    @PostMapping("platform/in/ding/talk/send/card/msg")
    ApiResult<Long> sendCardMsg(@RequestParam("userIdList") List<String> userIdList,
                                @RequestParam("title") String title,
                                @RequestParam("markdown") String markdown,
                                @RequestParam("singleTitle") String singleTitle,
                                @RequestParam("singleUrl") String singleUrl);
    
}
