package com.amusing.start.search.service.impl;

import cn.hutool.json.JSONUtil;
import com.amusing.start.client.input.ShopChangeInput;
import com.amusing.start.client.input.ShopPageInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.search.constant.SearchConstant;
import com.amusing.start.search.enums.SearchCode;
import com.amusing.start.search.enums.YesNo;
import com.amusing.start.search.exception.SearchException;
import com.amusing.start.search.service.IShopService;
import com.amusing.start.search.utils.SearchUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lv.qingyu
 */
@Slf4j
@Service
public class ShopServiceImpl implements IShopService {

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ShopServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    private static final String ID = "id";
    private static final String IS_DEL = "isDel";
    private static final String SHOP_NAME = "shopName";
    private static final String CREATE_TIME = "createTime";

    @Override
    public boolean change(ShopChangeInput input) throws SearchException {
        IndexRequest request = new IndexRequest(SearchConstant.SHOP_INDEX);
        request.id(input.getShopId());
        String jsonStr = JSONUtil.toJsonStr(input);
        request.source(jsonStr, XContentType.JSON);

        IndexResponse response;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[search]-shop change err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            throw new SearchException(SearchCode.SEARCH_SERVER_ERR);
        }

        if (response == null) {
            log.warn("[search]-shop change response is null! param:{}", input);
            throw new SearchException(SearchCode.RESPONSE_IS_NULL);
        }
        log.info("[search]-shop change param:{}, response:{}", input, response);
        DocWriteResponse.Result result = response.getResult();
        if (result == DocWriteResponse.Result.CREATED || result == DocWriteResponse.Result.UPDATED) {
            return true;
        }
        return false;
    }

    @Override
    public List<ShopOutput> shopPage(ShopPageInput input) throws SearchException {
        SearchSourceBuilder builder = buildShopPageQuery(input);
        SearchRequest request = buildSearchRequest(builder);

        SearchResponse response = doQuery(request);

        List<ShopOutput> result = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();

        for (SearchHit hit : hits) {
            String jsonStr = hit.getSourceAsString();
            ShopOutput shopOutput = JSONUtil.toBean(jsonStr, ShopOutput.class);
            result.add(shopOutput);
        }
        return result;
    }

    @Override
    public ShopOutput getDetail(String id) throws SearchException {
        SearchSourceBuilder builder = buildDetailQuery(id);
        SearchRequest request = buildSearchRequest(builder);

        SearchResponse response = doQuery(request);

        SearchHit[] hits = response.getHits().getHits();
        if (hits == null || hits.length <= SearchConstant.ZERO) {
            throw new SearchException(SearchCode.SHOP_NOT_FOUND);
        }
        String jsonStr = hits[0].getSourceAsString();
        return JSONUtil.toBean(jsonStr, ShopOutput.class);
    }

    // ---------------------------------- private ---------------------------------------------------

    private SearchRequest buildSearchRequest(SearchSourceBuilder builder) {
        return new SearchRequest(SearchConstant.SHOP_INDEX).source(builder);
    }

    private SearchSourceBuilder buildShopPageQuery(ShopPageInput input) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(IS_DEL, SearchConstant.ZERO));
        Optional.of(input).map(ShopPageInput::getName).filter(StringUtils::isNotEmpty).ifPresent(name -> {
            queryBuilder.filter(QueryBuilders.matchPhraseQuery(SHOP_NAME, SearchUtils.getRegexpValue(name)));
        });
        Optional.of(input).map(ShopPageInput::getStartTime).filter(i -> i > SearchConstant.ZERO).ifPresent(time -> {
            queryBuilder.filter(QueryBuilders.rangeQuery(CREATE_TIME).gte(time));
        });
        Optional.of(input).map(ShopPageInput::getEndTime).filter(i -> i > SearchConstant.ZERO).ifPresent(time -> {
            queryBuilder.filter(QueryBuilders.rangeQuery(CREATE_TIME).lte(time));
        });
        builder.query(queryBuilder);

        Integer pageSize = Optional.of(input).map(ShopPageInput::getPageSize).orElse(SearchConstant.MAX_PAGE_SIZE);
        pageSize = SearchUtils.pageSizeFilter(pageSize);

        Integer pageNum = Optional.of(input).map(ShopPageInput::getPageNum).orElse(SearchConstant.MIN_PAGE_NUM);
        pageNum = SearchUtils.pageNumFilter(pageNum);

        builder.from(pageNum * pageSize);
        builder.size(pageSize);
        builder.sort(new FieldSortBuilder(CREATE_TIME).order(SortOrder.DESC));

        return builder;
    }

    private SearchSourceBuilder buildDetailQuery(String id) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(IS_DEL, YesNo.YES.getKey()))
                .filter(QueryBuilders.termQuery(ID, id));
        builder.query(queryBuilder);
        return builder;
    }

    private SearchResponse doQuery(SearchRequest request) throws SearchException {
        SearchResponse response;
        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("[search]-shop doQuery err! param:{}, msg:{}", request, Throwables.getStackTraceAsString(e));
            throw new SearchException(SearchCode.SEARCH_SERVER_ERR);
        }
        if (response == null) {
            log.error("[search]-shop doQuery response is null! param:{}", request);
            throw new SearchException(SearchCode.RESPONSE_IS_NULL);
        }
        int status = response.status().getStatus();
        if (status != RestStatus.OK.getStatus()) {
            log.warn("[search]-shop doQuery fail! param:{}, response:{}", request, response);
            throw new SearchException(SearchCode.SEARCH_SERVER_ERR);
        }
        return response;
    }


}
