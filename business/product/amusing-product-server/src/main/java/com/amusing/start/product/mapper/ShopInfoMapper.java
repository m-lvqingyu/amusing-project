package com.amusing.start.product.mapper;

import com.amusing.start.product.pojo.ShopInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据商铺ID，获取商铺信息
     *
     * @param shopId 商铺ID
     * @return
     */
    ShopInfo selectByShopId(@Param("shopId") String shopId);

}