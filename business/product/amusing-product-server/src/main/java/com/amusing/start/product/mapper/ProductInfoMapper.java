package com.amusing.start.product.mapper;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
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
    Integer insertSelective(ProductInfo record);

    /**
     * 更新商品信息
     *
     * @param record
     * @return
     */
    Integer updateByPrimaryKeySelective(ProductInfo record);

    /**
     * 批量扣减商品库存 (一次不能超过20条)
     *
     * @param inputList 商品信息集合
     * @return
     */
    Integer batchDeductionStock(@Param("inputList") List<StockDeductionInput> inputList);

    /**
     * 根据商铺ID和商品名称，判断名称是否已存在
     *
     * @param shopId      商铺ID
     * @param productName 商品名称
     * @return
     */
    String checkExistByShopIdAndName(@Param("shopId") String shopId, @Param("productName") String productName);

    /**
     * 根据商铺ID，更新商品状态
     *
     * @param shopId 商铺ID
     * @param status 状态
     * @return
     */
    Integer updateStatusByShopId(@Param("shopId") String shopId, @Param("status") Integer status);

    /**
     * 根据商品ID，获取商品信息
     *
     * @param productId 商品ID
     * @return
     */
    ProductOutput getDetailsById(@Param("productId") String productId);

    List<ProductInfo> getAll();

    ProductInfo getById(@Param("productId") String productId);


}