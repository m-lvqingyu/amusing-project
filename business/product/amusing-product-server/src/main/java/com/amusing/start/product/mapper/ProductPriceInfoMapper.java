package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ProductPriceInfo;
import org.apache.ibatis.annotations.Mapper;

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
    int insertSelective(ProductPriceInfo record);

    /**
     * 更新商品价格信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ProductPriceInfo record);

}