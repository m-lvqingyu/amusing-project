package com.amusing.start.product.mapper;

import com.amusing.start.product.entity.pojo.ProductPriceInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lvqingyu
 */
@Mapper
public interface ProductPriceInfoMapper {

    /**
     * 保存商品价格信息
     *
     * @param record
     * @return
     */
    Integer insert(ProductPriceInfo record);

    /**
     * 更新商品价格信息
     *
     * @param record
     * @return
     */
    Integer update(ProductPriceInfo record);


    /**
     * 根据商品ID，获取最新一条价格信息
     *
     * @param productId 商品ID
     * @return
     */
    ProductPriceInfo getLastPrice(@Param("productId") String productId);


}