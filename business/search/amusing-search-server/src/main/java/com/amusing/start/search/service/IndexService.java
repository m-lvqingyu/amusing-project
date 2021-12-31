package com.amusing.start.search.service;

import com.amusing.start.search.exception.SearchException;

import java.util.Map;

/**
 * @author lv.qingyu
 */
public interface IndexService {

    /**
     * 创建索引
     *
     * @param index      索引名称
     * @param properties 映射
     * @return true-成功 false-失败
     * @throws SearchException
     */
    boolean create(String index, Map<String, Object> properties) throws SearchException;

    /**
     * 索引是否存在
     *
     * @param index 索引
     * @return true-存在 false-不存在
     * @throws SearchException
     */
    boolean exist(String index) throws SearchException;
}
