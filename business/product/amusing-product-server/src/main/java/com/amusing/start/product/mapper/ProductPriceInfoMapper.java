package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ProductPriceInfo;
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
    Integer insertSelective(ProductPriceInfo record);

    /**
     * 更新商品价格信息
     *
     * @param record
     * @return
     */
    Integer updateByPrimaryKeySelective(ProductPriceInfo record);

    /**
     * 获取商品价格信息
     *
     * @param productId 商品ID
     * @param priceId   价格ID
     * @return
     */
    ProductPriceInfo selectByProductAndPriceId(@Param("productId") String productId, @Param("priceId") String priceId);

    /**
     * 根据商品ID，获取最新一条价格信息
     *
     * @param productId 商品ID
     * @return
     */
    ProductPriceInfo selectLastRecordByProductId(@Param("productId") String productId);


}