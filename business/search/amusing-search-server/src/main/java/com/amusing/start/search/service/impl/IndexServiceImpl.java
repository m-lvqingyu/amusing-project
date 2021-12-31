package com.amusing.start.search.service.impl;

import com.amusing.start.code.CommCode;
import com.amusing.start.search.enums.SearchCode;
import com.amusing.start.search.exception.SearchException;
import com.amusing.start.search.service.IndexService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lv.qingyu
 */
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public IndexServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    private static final String PROPERTIES = "properties";

    private static final int INITIAL_CAPACITY = 2;

    @Override
    public boolean create(String index, Map<String, Object> properties) throws SearchException {
        if (exist(index)) {
            log.warn("[search]-create index index is exist! index:{}", index);
            throw new SearchException(SearchCode.INDEX_IS_EXIST);
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        Map<String, Object> mapping = new HashMap<>(INITIAL_CAPACITY);
        mapping.put(PROPERTIES, properties);
        request.mapping(mapping);
        CreateIndexResponse response = null;
        try {
            response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("[search]-create index err! indexName:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
        }
        log.info("[search]-create index indexName:{}, result:{}", index, response);
        return Optional.ofNullable(response)
                .map(CreateIndexResponse::isAcknowledged)
                .orElseThrow(() -> new SearchException(SearchCode.RESPONSE_IS_NULL));
    }

    @Override
    public boolean exist(String index) throws SearchException {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[search]-check index exist err! index:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
            throw new SearchException(CommCode.FAIL);
        }
    }


}
