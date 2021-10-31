package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lvqingyu
 */
@Mapper
public interface ProductInfoMapper {

    /**
     * 保存商品信息
     *
     * @param record
     * @return
     */
    int insertSelective(ProductInfo record);

    /**
     * 更新商品信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ProductInfo record);

    /**
     * 获取商品信息
     *
     * @param shopId    商铺ID
     * @param productId 商品ID
     * @return
     */
    ProductInfo selectByShopAndProductId(@Param("shopId") String shopId, @Param("productId") String productId);

}