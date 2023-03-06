package com.amusing.start.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.constant.CacheKey;
import com.amusing.start.product.constant.ProductConstant;
import com.amusing.start.product.entity.dto.ProductCreateDto;
import com.amusing.start.product.entity.dto.ShopCarDto;
import com.amusing.start.product.entity.pojo.ShopInfo;
import com.amusing.start.product.enums.ProductStatus;
import com.amusing.start.product.enums.YesNo;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.mapper.ProductPriceInfoMapper;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.entity.pojo.ProductInfo;
import com.amusing.start.product.entity.pojo.ProductPriceInfo;
import com.amusing.start.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private ShopInfoMapper shopInfoMapper;

    @Resource
    private ProductPriceInfoMapper productPriceInfoMapper;

    private final RedissonClient redissonClient;

    @Autowired
    public ProductServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(String userId, ProductCreateDto dto) throws CustomException {
        if (dto.getPrice().compareTo(BigDecimal.ZERO) > ProductConstant.ZERO) {
            throw new CustomException(ErrorCode.PRODUCT_PRICE_ERR);
        }
        ShopInfo shopInfo = shopInfoMapper.selectById(dto.getShopId());
        if (shopInfo == null) {
            throw new CustomException(ErrorCode.SHOP_NOT_FOUND);
        }
        ProductInfo info = productInfoMapper.selectByName(dto.getShopId(), dto.getName());
        if (info != null) {
            throw new CustomException(ErrorCode.PRODUCT_NAME_EXIST);
        }

        // 保存商品信息
        String productId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ProductInfo productInfo = ProductInfo.builder()
                .shopId(dto.getShopId())
                .id(productId)
                .name(dto.getName())
                .stock(dto.getStock())
                .status(ProductStatus.VALID.getCode())
                .isDel(YesNo.YES.getKey())
                .describe(dto.getDescribe())
                .createBy(userId)
                .createTime(timeMillis)
                .updateBy(userId)
                .updateTime(timeMillis)
                .build();
        Integer result = productInfoMapper.insert(productInfo);
        if (result <= ProductConstant.ZERO) {
            throw new CustomException(ErrorCode.PRODUCT_CREATE_ERR);
        }

        // 保存商品价格
        String priceId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        ProductPriceInfo priceInfo = ProductPriceInfo.builder()
                .priceId(priceId)
                .productId(productId)
                .version(ProductConstant.ZERO)
                .price(dto.getPrice())
                .createBy(userId)
                .createTime(timeMillis)
                .updateBy(userId)
                .updateTime(timeMillis)
                .build();
        result = productPriceInfoMapper.insert(priceInfo);
        if (result <= ProductConstant.ZERO) {
            throw new CustomException(ErrorCode.PRODUCT_CREATE_ERR);
        }
        return productId;
    }

    @Override
    public List<ShopCarOutput> shopCar(String userId) throws CustomException {
        String cacheKey = CacheKey.getShopCar(userId);
        RBucket<List<ShopCarDto>> bucket = redissonClient.getBucket(cacheKey);
        List<ShopCarDto> shopCarDtoList = bucket.get();
        if (CollectionUtil.isEmpty(shopCarDtoList)) {
            throw new CustomException(ErrorCode.SHOP_CAR_ERROR);
        }
        List<ShopCarOutput> outputList = new ArrayList<>();
        for (ShopCarDto shopCarDto : shopCarDtoList) {
            String shopId = shopCarDto.getShopId();
            ShopInfo shopInfo = shopInfoMapper.selectById(shopId);
            if (shopInfo == null) {
                throw new CustomException(ErrorCode.SHOP_NOT_FOUND);
            }
            List<ShopCarDto.ProductDto> productList = shopCarDto.getProductList();
            for (ShopCarDto.ProductDto productDto : productList) {
                String productId = productDto.getProductId();
                ProductInfo productInfo = productInfoMapper.getById(productId);
                if (productInfo == null) {
                    throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
                }
                ProductPriceInfo productPriceInfo = productPriceInfoMapper.getLastPrice(productId);
                if (productPriceInfo == null) {
                    throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
                }
                Integer num = productDto.getNum();
                ShopCarOutput shopCarOutput = ShopCarOutput.builder()
                        .shopId(shopId)
                        .shopName(shopInfo.getShopName())
                        .productId(productId)
                        .productName(productInfo.getName())
                        .priceId(productPriceInfo.getPriceId())
                        .price(productPriceInfo.getPrice())
                        .stock(num)
                        .build();
                outputList.add(shopCarOutput);
            }
        }
        return outputList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean deductionStock(List<StockDeductionInput> inputs) throws CustomException {
        Integer result = productInfoMapper.batchDeductionStock(inputs);
        if (result == null || result != inputs.size()) {
            throw new CustomException(ErrorCode.PRODUCT_DEDUCTION_STOCK);
        }
        return ProductConstant.TRUE;
    }

}
