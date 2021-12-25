package com.amusing.start.product.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.product.constant.ProductConstant;
import com.amusing.start.product.dto.create.ProductCreateDto;
import com.amusing.start.product.enums.ProductCode;
import com.amusing.start.product.enums.ProductStatus;
import com.amusing.start.product.enums.YesNo;
import com.amusing.start.product.exception.ProductException;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.mapper.ProductPriceInfoMapper;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.pojo.ProductInfo;
import com.amusing.start.product.pojo.ProductPriceInfo;
import com.amusing.start.product.pojo.ShopInfo;
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductInfoMapper productInfoMapper;
    private final ShopInfoMapper shopInfoMapper;
    private final ProductPriceInfoMapper productPriceInfoMapper;

    @Autowired
    public ProductServiceImpl(ProductInfoMapper productInfoMapper, ShopInfoMapper shopInfoMapper, ProductPriceInfoMapper productPriceInfoMapper) {
        this.productInfoMapper = productInfoMapper;
        this.shopInfoMapper = shopInfoMapper;
        this.productPriceInfoMapper = productPriceInfoMapper;
    }

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @Override
    public boolean deductionStock(List<StockDeductionInput> inputs) throws ProductException {
        Integer result = null;
        try {
            result = productInfoMapper.batchDeductionStock(inputs);
        } catch (Exception e) {
            log.error("[product]-batchDeductionStock err! param:{}, msg:{}", inputs, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i > ProductConstant.ZERO).orElseThrow(() -> new ProductException(CommCode.FAIL));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.LOCAL)
    @Override
    public String create(String executor, ProductCreateDto createDto) throws ProductException {
        // 校验商品价格是否合法
        Optional.ofNullable(createDto.getProductPrice()).filter(i -> i.compareTo(BigDecimal.ZERO) > ProductConstant.ZERO).orElseThrow(() -> new ProductException(ProductCode.PRODUCT_PRICE_ERR));
        // 判断商铺是否存在
        String queryShopId = shopInfoMapper.checkExistById(createDto.getShopId());
        Optional.ofNullable(queryShopId).filter(StringUtils::isNotEmpty).orElseThrow(() -> new ProductException(ProductCode.SHOP_NOT_FOUND));
        // 判断商品名称是否已经存在
        String queryProductId = productInfoMapper.checkExistByShopIdAndName(createDto.getShopId(), createDto.getProductName());
        if (StringUtils.isNotEmpty(queryProductId)) {
            throw new ProductException(ProductCode.PRODUCT_NAME_EXIST);
        }
        // 保存商品信息
        String productId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ProductInfo productInfo = ProductInfo.builder().shopId(createDto.getShopId()).productId(productId).productName(createDto.getProductName()).productStock(createDto.getProductStock()).status(ProductStatus.VALID.getCode()).isDel(YesNo.YES.getKey()).describe(createDto.getDescribe()).createBy(executor).createTime(timeMillis).updateBy(executor).updateTime(timeMillis).build();
        Integer result = productInfoMapper.insertSelective(productInfo);
        Optional.ofNullable(result).filter(i -> i > ProductConstant.ZERO).orElseThrow(() -> new ProductException(ProductCode.PRODUCT_CREATE_ERR));
        // 保存商品价格
        String priceId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        ProductPriceInfo priceInfo = ProductPriceInfo.builder().priceId(priceId).productId(productId).version(ProductConstant.ZERO).price(createDto.getProductPrice()).createBy(executor).createTime(timeMillis).updateBy(executor).updateTime(timeMillis).build();
        result = productPriceInfoMapper.insertSelective(priceInfo);
        Optional.ofNullable(result).filter(i -> i > ProductConstant.ZERO).orElseThrow(() -> new ProductException(ProductCode.PRODUCT_CREATE_ERR));
        return productId;
    }

    @Override
    public Map<String, ProductOutput> getProductDetails(Set<String> productIds) {
        List<ProductInfo> productInfoList = productInfoMapper.getDetailsByIds(productIds);
        Map<String, ProductOutput> result = new HashMap<>();
        Optional.ofNullable(productInfoList).ifPresent(list -> {
            list.forEach(i -> {
                ProductPriceInfo priceInfo = productPriceInfoMapper.selectLastRecordByProductId(i.getProductId());
                Optional.ofNullable(priceInfo).ifPresent(info -> {
                    ProductOutput output = ProductOutput.builder().shopId(i.getShopId()).productId(i.getProductId()).productName(i.getProductName()).productStock(i.getProductStock()).priceId(info.getPriceId()).price(info.getPrice()).build();
                    result.put(i.getProductId(), output);
                });
            });
        });
        return result;
    }

    @Override
    public Map<String, ShopOutput> getShopDetails(Set<String> shopIds) {
        List<ShopInfo> shopInfoList = shopInfoMapper.getDetailsByIds(shopIds);
        Map<String, ShopOutput> result = new HashMap<>();
        Optional.ofNullable(shopInfoList).ifPresent(list -> {
            list.forEach(i -> {
                ShopOutput shopOutput = ShopOutput.builder().shopId(i.getShopId()).shopName(i.getShopName()).grade(i.getGrade()).build();
                result.put(i.getShopId(), shopOutput);
            });
        });
        return result;
    }

}
