package com.amusing.start.product.mapper;

import com.amusing.start.product.entity.pojo.ShopInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
    Integer insertSelective(ShopInfo record);

    /**
     * 更新商铺信息
     *
     * @param record
     * @return
     */
    Integer updateByPrimaryKeySelective(ShopInfo record);

    /**
     * 根据商铺ID，获取商铺信息
     *
     * @param shopId 商铺ID
     * @return
     */
    ShopInfo selectById(@Param("shopId") String shopId);


    /**
     * 根据商铺名称，判断名称是否已经存在
     *
     * @param shopName 商铺名称
     * @return
     */
    String checkExistByName(@Param("shopName") String shopName);

    /**
     * 根据商铺ID，获取商铺状态
     *
     * @param shopId 商铺ID
     * @return
     */
    Integer selectStatusById(@Param("shopId") String shopId);

    /**
     * 根据商铺ID，获取商铺信息详情
     *
     * @param shopIds 商铺ID集合
     * @return
     */
    List<ShopInfo> getDetailsByIds(Set<String> shopIds);

}