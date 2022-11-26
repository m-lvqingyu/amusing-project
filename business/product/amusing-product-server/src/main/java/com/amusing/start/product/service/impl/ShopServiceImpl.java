package com.amusing.start.product.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.entity.dto.ShopCreateDto;
import com.amusing.start.product.enums.ShopStatus;
import com.amusing.start.product.enums.YesNo;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.entity.pojo.ShopInfo;
import com.amusing.start.product.service.IShopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@Service
public class ShopServiceImpl implements IShopService {

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @Override
    public String create(String executor, ShopCreateDto dto) throws CustomException {
        String queryShopId = shopInfoMapper.checkExistByName(dto.getShopName());
        if (StringUtils.isNotEmpty(queryShopId)) {
            throw new CustomException(ErrorCode.SHOP_NAME_EXIST);
        }
        String shopId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ShopInfo shopInfo = ShopInfo.builder()
                .shopId(shopId)
                .shopName(dto.getShopName())
                .grade(dto.getGrade())
                .status(ShopStatus.VALID.getCode())
                .isDel(YesNo.YES.getKey())
                .createBy(executor)
                .createTime(timeMillis)
                .updateBy(executor)
                .updateTime(timeMillis)
                .build();
        Integer result = shopInfoMapper.insertSelective(shopInfo);
        if (result == null || result <= 0) {
            throw new CustomException(ErrorCode.SHOP_CREATE_ERR);
        }
        return shopId;
    }

}
