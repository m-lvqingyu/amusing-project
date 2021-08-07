package com.amusing.start.message.api;

import com.amusing.start.message.input.NotifyInput;
import com.amusing.start.result.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Create By 2021/7/31
 *
 * @author lvqingyu
 */

public interface NotifyApi {

    /**
     * 创建消息
     *
     * @param notifyInput
     * @return
     */
    @PostMapping
    ApiResult create(@RequestBody NotifyInput notifyInput);

}
