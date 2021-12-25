package com.amusing.start.product.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.product.constant.ProductConstant;
import com.amusing.start.product.dto.create.ShopCreateDto;
import com.amusing.start.product.enums.ProductCode;
import com.amusing.start.product.enums.ProductStatus;
import com.amusing.start.product.enums.ShopStatus;
import com.amusing.start.product.enums.YesNo;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.pojo.ShopInfo;
import com.amusing.start.product.service.IShopService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author lv.qingyu
 * @version 1.0
 * @date 2021/12/24
 */
@Service
public class ShopServiceImpl implements IShopService {

    private final ShopInfoMapper shopInfoMapper;
    private final ProductInfoMapper productInfoMapper;

    @Autowired
    public ShopServiceImpl(ShopInfoMapper shopInfoMapper, ProductInfoMapper productInfoMapper) {
        this.shopInfoMapper = shopInfoMapper;
        this.productInfoMapper = productInfoMapper;
    }

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @Override
    public String create(String executor, ShopCreateDto createDto) throws ProductException {
        String queryShopId = shopInfoMapper.checkExistByName(createDto.getShopName());
        if(StringUtils.isNotEmpty(queryShopId)){
            throw new ProductException(ProductCode.SHOP_NAME_EXIST);
        }

        String shopId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ShopInfo shopInfo = ShopInfo.builder()
                .shopId(shopId)
                .shopName(createDto.getShopName())
                .grade(createDto.getGrade())
                .status(ShopStatus.VALID.getCode())
                .isDel(YesNo.YES.getKey())
                .createBy(executor)
                .createTime(timeMillis)
                .updateBy(executor)
                .updateTime(timeMillis)
                .build();

        Integer result = shopInfoMapper.insertSelective(shopInfo);
        Optional.ofNullable(result).filter(i -> i > ProductConstant.ZERO)
                .orElseThrow(() -> new ProductException(ProductCode.SHOP_CREATE_ERR));
        return shopId;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @Override
    public void close(String executor, String shopId) throws ProductException {
        Integer status = shopInfoMapper.selectStatusById(shopId);
        Optional.ofNullable(status).filter(i -> i == ShopStatus.VALID.getCode())
                .orElseThrow(() -> new ProductException(ProductCode.SHOP_NOT_FOUND));

        productInfoMapper.updateStatusByShopId(shopId, ProductStatus.INVALID.getCode());

        ShopInfo updateShopInfo = ShopInfo.builder()
                .shopId(shopId)
                .status(ShopStatus.INVALID.getCode())
                .updateBy(executor)
                .updateTime(System.currentTimeMillis())
                .build();
        shopInfoMapper.updateByPrimaryKeySelective(updateShopInfo);
    }

}
