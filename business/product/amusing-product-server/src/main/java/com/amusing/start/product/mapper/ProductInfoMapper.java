package com.amusing.start.product.mapper;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.product.pojo.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 批量扣减商品库存 (一次不能超过20条)
     *
     * @param inputList 商品信息集合
     * @return
     */
    Integer batchDeductionStock(@Param("shopId") List<StockDeductionInput> inputList);

}