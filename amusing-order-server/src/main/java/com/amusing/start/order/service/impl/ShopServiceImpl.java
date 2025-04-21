package com.amusing.start.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.config.props.SnowflakeProps;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.enums.ShopStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.mapper.ShopMapper;
import com.amusing.start.order.pojo.Shop;
import com.amusing.start.order.req.AdminShopCreateReq;
import com.amusing.start.order.service.ShopService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Lv.QingYu
 * @since 2021/12/24
 */
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopMapper shopMapper;

    private final SnowflakeProps props;

    @Override
    public void create(String userId, AdminShopCreateReq req) {
        if (getByName(req.getName()) != null) {
            throw new CustomException(OrderErrorCode.SHOP_NAME_EXIST);
        }
        if (shopMapper.insert(build(userId, req)) <= 0) {
            throw new CustomException(OrderErrorCode.PRODUCT_CREATE_ERR);
        }
    }

    @Override
    public Shop getById(String shopId) {
        return shopMapper.selectOne(new LambdaQueryWrapper<Shop>()
                .eq(Shop::getId, shopId)
                .eq(Shop::getIsDel, YesNo.NO.getKey())
        );
    }

    @Override
    public Shop getByName(String name) {
        return shopMapper.selectOne(new LambdaQueryWrapper<Shop>()
                .eq(Shop::getName, name)
                .eq(Shop::getIsDel, YesNo.NO.getKey())
        );
    }

    private Shop build(String createUserId, AdminShopCreateReq req) {
        long timeMillis = System.currentTimeMillis();
        return new Shop()
                .setId(IdUtil.createSnowflake(props.getWorker(), props.getCenter()).nextIdStr())
                .setName(req.getName())
                .setStatus(ShopStatus.VALID.getCode())
                .setIsDel(YesNo.YES.getKey())
                .setCreateBy(createUserId)
                .setCreateTime(timeMillis)
                .setUpdateBy(createUserId)
                .setUpdateTime(timeMillis);
    }

}
