package com.amusing.start.product.service;

import com.amusing.start.product.dto.create.ShopCreateDto;
import com.amusing.start.product.exception.ProductException;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
public interface IShopService {

    /**
     * 新增商铺
     *
     * @param executor  执行人
     * @param createDto 商铺信息
     * @return
     * @throws ProductException
     */
    String create(String executor, ShopCreateDto createDto) throws ProductException;

    /**
     * 关闭商铺
     *
     * @param executor 执行人
     * @param shopId   商铺ID
     * @throws ProductException
     */
    void close(String executor, String shopId) throws ProductException;

}
