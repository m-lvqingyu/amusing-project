package com.amusing.start.search.service.impl;

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

@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public IndexServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    private static final String PROPERTIES = "properties";

    @Override
    public Boolean create(String index, Map<String, Object> properties) {
        Boolean exist = exist(index);
        if (exist == null) {
            log.warn("[search]-create index check exist result is null! index:{}", index);
            return false;
        }
        if (exist) {
            log.warn("[search]-create index index is exist! index:{}", index);
            return true;
        }
        CreateIndexRequest request = new CreateIndexRequest(index);
        Map<String, Object> mapping = new HashMap<>();
        mapping.put(PROPERTIES, properties);
        request.mapping(mapping);
        try {
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (response == null) {
                log.warn("[search]-create index result is null! indexName:{}", index);
                return false;
            }
            log.info("[search]-create index indexName:{}, result:{}", index, response);
            return response.isAcknowledged();
        } catch (Exception e) {
            log.error("[search]-create index err! indexName:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
        } finally {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                log.error("[search]-create index close client err! index:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
            }
        }
        return false;
    }

    @Override
    public Boolean exist(String index) {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[search]-check index exist err! index:{}, msg:{}", index, Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
