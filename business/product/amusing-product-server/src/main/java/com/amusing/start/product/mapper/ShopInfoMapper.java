package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ShopInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lvqingyu
 */
@Mapper
public interface ShopInfoMapper {

    /**
     * 保存商铺信息
     *
     * @param record
     * @return
     */
    int insertSelective(ShopInfo record);

    /**
     * 更新商铺信息
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(ShopInfo record);

}