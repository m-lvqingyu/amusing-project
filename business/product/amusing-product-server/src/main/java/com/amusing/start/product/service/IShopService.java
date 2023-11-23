package com.amusing.start.product.service;

import com.amusing.start.product.entity.pojo.ShopInfo;

/**
 * @author Lv.QingYu
 * @description: 商铺服务Service
 * @since 2021/12/24
 */
public interface IShopService {

    /**
     * @param shopId 商铺ID
     * @return 商铺信息
     * @description: 根据商铺ID，获取商铺信息
     */
    ShopInfo getById(String shopId);

    /**
     * @param name 商铺名称
     * @return 商铺ID
     * @description: 根据名称获取商铺ID
     */
    String nameExist(String name);

    /**
     * @param shopInfo 商铺信息
     * @return 新增条数
     * @description: 保存商铺信息
     */
    Integer insert(ShopInfo shopInfo);

}
