package com.amusing.start.search.service;

import com.amusing.start.client.input.ShopPageInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.search.exception.SearchException;

import java.util.List;

/**
 * @author lv.qingyu
 */
public interface IShopService {

    /**
     * 分页获取商铺信息集合
     *
     * @param input 查询条件
     * @return 商铺信息集合
     * @throws SearchException
     */
    List<ShopOutput> shopPage(ShopPageInput input) throws SearchException;

    /**
     * 获取商铺信息
     *
     * @param id 商铺ID
     * @return 商铺信息
     * @throws SearchException
     */
    ShopOutput getDetail(String id) throws SearchException;

}
