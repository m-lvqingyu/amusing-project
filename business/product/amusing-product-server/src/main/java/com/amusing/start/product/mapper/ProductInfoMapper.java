package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ProductInfo;
import org.apache.ibatis.annotations.Mapper;

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

}