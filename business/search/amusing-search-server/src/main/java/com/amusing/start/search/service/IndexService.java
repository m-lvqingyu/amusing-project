package com.amusing.start.search.service;

import java.util.Map;

public interface IndexService {

    /**
     * 创建索引
     *
     * @param index      索引名称
     * @param properties 映射
     * @return
     */
    Boolean create(String index, Map<String, Object> properties);

    /**
     * 索引是否存在
     *
     * @param index 索引
     * @return
     */
    Boolean exist(String index);
}
