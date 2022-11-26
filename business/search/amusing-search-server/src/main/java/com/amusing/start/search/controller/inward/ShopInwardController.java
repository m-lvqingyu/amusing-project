package com.amusing.start.search.controller.inward;

import com.amusing.start.client.api.ShopSearchClient;
import com.amusing.start.search.service.IShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lv.qingyu
 */
@Slf4j
@RestController
public class ShopInwardController implements ShopSearchClient {

    private final IShopService shopService;

    @Autowired
    public ShopInwardController(IShopService shopService) {
        this.shopService = shopService;
    }

}
