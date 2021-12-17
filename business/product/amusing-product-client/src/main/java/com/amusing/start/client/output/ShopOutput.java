package com.amusing.start.client.output;

import lombok.Data;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
public class ShopOutput {

    /**
     * 商铺ID
     */
    private String shopId;

    /**
     * 商铺名称
     */
    private String shopName;

    /**
     * 等级
     */
    private Integer grade;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 商品信息集合
     */
    List<ProductOutput> productList;


}
