package com.amusing.start.search.service.impl;

import com.amusing.start.search.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
