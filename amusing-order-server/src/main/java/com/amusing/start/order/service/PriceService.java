package com.amusing.start.order.service;

import com.amusing.start.order.pojo.Price;

/**
 * @author Lv.QingYu
 * @since 2023/9/21
 */
public interface PriceService {

    void insert(Price productPriceInfo);

    Price getLastPrice(String productId);


}
