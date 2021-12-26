package com.amusing.start.search.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author lv.qingyu
 */
@Configuration
public class EsConfig extends AbstractElasticsearchConfiguration {

    /**
     * es服务地址
     */
    @Value("${es.address}")
    private String esAddress;

    /**
     * es客户端配置
     *
     * @return
     */
    @Bean(name = "restHighLevelClient")
    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration configuration = ClientConfiguration.builder().connectedTo(esAddress).build();
        return RestClients.create(configuration).rest();
    }

}
