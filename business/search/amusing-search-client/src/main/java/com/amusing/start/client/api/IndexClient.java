package com.amusing.start.client.api;

import com.amusing.start.client.fallback.IndexClientFallback;
import com.amusing.start.client.input.IndexCreateInput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author lv.qingyu
 */
@FeignClient(value = "amusing-search-server", fallbackFactory = IndexClientFallback.class)
public interface IndexClient {

    /**
     * 创建索引
     *
     * @param input 索引信息
     * @return true:创建成功 false:创建失败
     */
    @PostMapping("/search/inward/index/v1/create")
    ApiResult<Boolean> create(@Valid @RequestBody IndexCreateInput input);

    /**
     * 索引是否存在
     *
     * @param index 索引
     * @return true:已经存在 false:不存在
     */
    @GetMapping("/search/inward/index/v1/exist/{index}")
    ApiResult<Boolean> exist(@PathVariable("index") String index);

}
