package com.amusing.start.message.controller;

import com.amusing.start.result.ApiResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Create By 2021/7/31
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/push")
public class MessageController {

    @Resource
    private Source source;

    @GetMapping
    public ApiResult push() throws InterruptedException {
        String msg = "hello word";
        for(int i = 0 ; i < 100 ; i++){
            Map<String, Object> headers = new HashMap<>();
            headers.put(MessageConst.PROPERTY_TAGS, "testTag");
            MessageHeaders messageHeaders = new MessageHeaders(headers);
            msg = msg + i;
            Message<String> message = MessageBuilder.createMessage(msg, messageHeaders);
            this.source.output().send(message);
            Thread.sleep(1000);
        }
        return ApiResult.ok();
    }
}
