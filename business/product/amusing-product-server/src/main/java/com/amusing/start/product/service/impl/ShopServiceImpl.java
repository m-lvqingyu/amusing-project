package com.amusing.start.product.service.impl;

import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.entity.pojo.ShopInfo;
import com.amusing.start.product.service.IShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @description: 商铺服务ServiceImpl
 * @since 2021/12/24
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Override
    public ShopInfo getById(String shopId) {
        return shopInfoMapper.selectById(shopId);
    }

    @Override
    public String nameExist(String name) {
        return shopInfoMapper.checkExistByName(name);
    }

    @Override
    public Integer insert(ShopInfo shopInfo) {
        return shopInfoMapper.insertSelective(shopInfo);
    }

}
