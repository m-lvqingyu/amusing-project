package com.amusing.start.product.service;

import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ShopCreateDto;

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
     * @throws CustomException
     */
    String create(String executor, ShopCreateDto createDto) throws CustomException;


}
