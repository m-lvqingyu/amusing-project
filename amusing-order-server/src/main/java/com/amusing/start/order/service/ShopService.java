package com.amusing.start.order.service;

import com.amusing.start.order.pojo.Shop;
import com.amusing.start.order.req.AdminShopCreateReq;

/**
 * @author Lv.QingYu
 * @since 2021/12/24
 */
public interface ShopService {

    /**
     * 新增商铺
     *
     * @param userId 执行人
     * @param req    商铺信息
     */
    void create(String userId, AdminShopCreateReq req);

    /**
     * 根据商铺ID，获取商铺信息
     *
     * @param shopId 商铺ID
     * @return 商铺信息
     */
    Shop getById(String shopId);

    Shop getByName(String name);

}
