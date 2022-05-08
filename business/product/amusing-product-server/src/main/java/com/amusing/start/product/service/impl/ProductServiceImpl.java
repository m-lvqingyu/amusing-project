package com.amusing.start.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
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
import com.amusing.start.product.service.IProductService;
import com.google.common.base.Throwables;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final RedissonClient redissonClient;

    private final static String PRODUCT_STOCK_CACHE = "product:stock";

    private final static String PRODUCT_STOCK_LOCK_PREFIX = "product:stock:lock:";

    @Autowired
    public ProductServiceImpl(ProductInfoMapper productInfoMapper,
                              ShopInfoMapper shopInfoMapper,
                              ProductPriceInfoMapper productPriceInfoMapper,
                              RedissonClient redissonClient) {
        this.productInfoMapper = productInfoMapper;
        this.shopInfoMapper = shopInfoMapper;
        this.productPriceInfoMapper = productPriceInfoMapper;
        this.redissonClient = redissonClient;
    }

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @PostConstruct
    public void init() {
        List<ProductInfo> infoList = productInfoMapper.getAll();
        if (CollectionUtil.isEmpty(infoList)) {
            return;
        }
        Map<String, Long> stockMap = new HashMap<>(infoList.size());
        infoList.forEach(i -> {
            String productId = i.getProductId();
            long stock = i.getProductStock().longValue();
            stockMap.put(productId, stock);
        });
        RMap<String, Long> clientMap = redissonClient.getMap(PRODUCT_STOCK_CACHE);
        clientMap.putAll(stockMap);
    }

    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deductionStock(List<StockDeductionInput> inputs) throws ProductException {
        Integer result = null;
        try {
            result = productInfoMapper.batchDeductionStock(inputs);
        } catch (Exception e) {
            log.error("[product]-batchDeductionStock err! param:{}, msg:{}",
                    inputs,
                    Throwables.getStackTraceAsString(e));
        }
        if (result == null || result != inputs.size()) {
            throw new ProductException(ProductCode.PRODUCT_DEDUCTION_STOCK);
        }

        return ProductConstant.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
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
    public List<ProductOutput> productDetails(Set<String> productIds) {
        List<ProductOutput> result = new ArrayList<>();
        productIds.forEach(i -> {
            ProductOutput output = productInfoMapper.getDetailsById(i);
            if (output != null) {
                result.add(output);
            }
        });
        return result;
    }

    @Override
    public Map<String, Long> productStock(Set<String> productIds) {
        RMap<String, Long> clientMap = redissonClient.getMap(PRODUCT_STOCK_CACHE);
        Map<String, Long> stockMap = clientMap.getAll(productIds);
        for (String productId : productIds) {
            if (stockMap.containsKey(productId)) {
                continue;
            }
            RLock rLock = redissonClient.getLock(PRODUCT_STOCK_LOCK_PREFIX + productId);
            try {
                if (rLock.tryLock(ProductConstant.ONE, TimeUnit.SECONDS)) {
                    Long currentStock = clientMap.get(productId);
                    if (currentStock != null) {
                        stockMap.put(productId, currentStock);
                        continue;
                    }
                    ProductInfo productInfo = productInfoMapper.getById(productId);
                    if (productInfo == null) {
                        continue;
                    }
                    long stock = productInfo.getProductStock().longValue();
                    clientMap.put(productId, stock);
                    stockMap.put(productId, stock);
                }
            } catch (Exception e) {
                log.error("[Product]-productStock err! msg:{}", Throwables.getStackTraceAsString(e));
            } finally {
                rLock.unlock();
            }
        }
        return stockMap;
    }

    @Override
    public Boolean updateStockCache(Set<String> productIds) {
        RMap<String, Long> clientMap = redissonClient.getMap(PRODUCT_STOCK_CACHE);
        productIds.forEach(i -> {
            ProductInfo productInfo = productInfoMapper.getById(i);
            clientMap.put(i, productInfo.getProductStock().longValue());
        });
        return ProductConstant.TRUE;
    }


}
