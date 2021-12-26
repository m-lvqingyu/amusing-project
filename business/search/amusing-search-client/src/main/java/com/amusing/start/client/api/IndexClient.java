package com.amusing.start.client.api;

import com.amusing.start.client.fallback.IndexClientFallback;
import com.amusing.start.client.input.IndexCreateInput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "amusing-search-server", fallbackFactory = IndexClientFallback.class)
public interface IndexClient {

    /**
     * 创建索引
     *
     * @param input 索引信息
     * @return
     */
    @PostMapping("/search/inward/v1/create")
    Boolean create(@RequestBody IndexCreateInput input);

    /**
     * 索引是否存在
     *
     * @param index 索引
     * @return
     */
    @GetMapping("/search/inward/v1/exist/{index}")
    Boolean exist(@PathVariable("index") String index);

}
