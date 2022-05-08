package com.amusing.start.order.service;

import java.util.Map;

/**
 * @author ：lv.qingyu
 * @date ：2022/4/9 17:56
 */
public interface IShopCarService {

    /**
     * 购物车-添加/修改商品
     *
     * @param userId     用户id
     * @param productId  商品Id
     * @param productNum 商品数量
     * @return true:成功 false:失败
     */
    boolean operation(String userId, String productId, Integer productNum);

    boolean del(String userId, String productId);

    Map<String, Integer> get(String userId);
}
