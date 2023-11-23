package com.amusing.start.product.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.constant.CacheKey;
import com.amusing.start.product.entity.dto.ProductCreateDto;
import com.amusing.start.product.entity.dto.ShopCarCreateDto;
import com.amusing.start.product.entity.dto.ShopCarDto;
import com.amusing.start.product.entity.dto.ShopCreateDto;
import com.amusing.start.product.entity.pojo.ProductInfo;
import com.amusing.start.product.entity.pojo.ProductPriceInfo;
import com.amusing.start.product.entity.pojo.ShopInfo;
import com.amusing.start.product.enums.ProductErrorCode;
import com.amusing.start.product.enums.ProductStatus;
import com.amusing.start.product.enums.ShopStatus;
import com.amusing.start.product.enums.YesNo;
import com.amusing.start.product.service.IProductPriceService;
import com.amusing.start.product.service.IProductService;
import com.amusing.start.product.service.IShopService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @description: 商品服务业务层
 * @since 2023/9/20
 */
@Component
public class ProductBusiness {

    private final IProductService productService;

    private final IShopService shopService;

    private final IProductPriceService productPriceService;

    private final RedissonClient redissonClient;

    @Autowired
    public ProductBusiness(IProductService productService,
                           IShopService shopService,
                           IProductPriceService productPriceService,
                           RedissonClient redissonClient) {
        this.productService = productService;
        this.shopService = shopService;
        this.productPriceService = productPriceService;
        this.redissonClient = redissonClient;
    }

    @Value("${product.worker}")
    private Long productWorker;

    @Value("${product.dataCenter}")
    private Long productDataCenter;

    /**
     * @param userId 用户ID
     * @param dto    商品信息
     * @return 商品ID
     * @description: 新增商品
     */
    @Transactional(rollbackFor = Exception.class)
    public String productCreate(String userId, ProductCreateDto dto) {
        if (dto.getPrice() <= CommConstant.ZERO) {
            throw new CustomException(ProductErrorCode.PRODUCT_PRICE_ERR);
        }
        ShopInfo shopInfo = shopService.getById(dto.getShopId());
        if (shopInfo == null) {
            throw new CustomException(ProductErrorCode.SHOP_NOT_FOUND);
        }
        ProductInfo info = productService.getByName(dto.getShopId(), dto.getName());
        if (info != null) {
            throw new CustomException(ProductErrorCode.PRODUCT_NAME_EXIST);
        }
        // 保存商品信息
        String productId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ProductInfo productInfo = new ProductInfo().setShopId(dto.getShopId())
                .setId(productId)
                .setName(dto.getName())
                .setStock(dto.getStock())
                .setStatus(ProductStatus.VALID.getCode())
                .setIsDel(YesNo.YES.getKey())
                .setDescribe(dto.getDescribe())
                .setCreateBy(userId)
                .setCreateTime(timeMillis)
                .setUpdateBy(userId)
                .setUpdateTime(timeMillis);
        Integer result = productService.insert(productInfo);
        if (result == null || result <= 0) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        // 保存商品价格
        String priceId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        ProductPriceInfo priceInfo = ProductPriceInfo.builder()
                .priceId(priceId)
                .productId(productId)
                .version(CommConstant.ZERO)
                .price(dto.getPrice())
                .createBy(userId)
                .createTime(timeMillis)
                .updateBy(userId)
                .updateTime(timeMillis)
                .build();
        result = productPriceService.insert(priceInfo);
        if (result == null || result <= 0) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return productId;
    }

    /**
     * @param userId 用户ID
     * @param dto    购物车信息
     * @return true:成功 false:失败
     * @description: 购物车新增
     */
    public Boolean shopping(String userId, ShopCarCreateDto dto) {
        ProductInfo productInfo = productService.getById(dto.getProductId());
        String cacheKey = CacheKey.getShopCar(userId);
        RBucket<List<ShopCarDto>> bucket = redissonClient.getBucket(cacheKey);
        List<ShopCarDto> dtoList = bucket.get();
        dtoList = build(dtoList, productInfo, dto);
        bucket.set(dtoList, CacheKey.SHOP_CAR_TIME_OUT, TimeUnit.DAYS);
        return Boolean.TRUE;
    }

    /**
     * @param userId 用户ID
     * @return 购物车信息
     * @description: 根据用户ID获取商品购物车信息
     */
    public List<ShopCarOutput> shopCar(String userId) {
        String cacheKey = CacheKey.getShopCar(userId);
        RBucket<List<ShopCarDto>> bucket = redissonClient.getBucket(cacheKey);
        List<ShopCarDto> shopCarDtoList = bucket.get();
        if (CollectionUtil.isEmpty(shopCarDtoList)) {
            return new ArrayList<>();
        }
        List<ShopCarOutput> outputList = new ArrayList<>();
        for (ShopCarDto shopCarDto : shopCarDtoList) {
            String shopId = shopCarDto.getShopId();
            ShopInfo shopInfo = shopService.getById(shopId);
            ShopCarOutput shopCarOutput = build(shopInfo, shopCarDto.getSort());
            List<ShopCarOutput.ShopCarProductOutput> productOutputList = build(shopCarDto.getProductList());
            shopCarOutput.setProductOutputList(productOutputList);
            outputList.add(shopCarOutput);
        }
        return outputList;
    }

    /**
     * @param inputs 商品ID和扣减数量集合
     * @return true：成功 false:失败
     * @description: 扣减商品库存
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deductionStock(List<StockDeductionInput> inputs) {
        Integer result = productService.batchDeductionStock(inputs);
        if (result == null || result != inputs.size()) {
            throw new CustomException(ProductErrorCode.PRODUCT_DEDUCTION_STOCK);
        }
        return Boolean.TRUE;
    }

    /**
     * @param executor 执行人ID
     * @param dto      商铺信息
     * @return 商铺ID
     * @description: 商铺新增
     */
    public String createShop(String executor, ShopCreateDto dto) {
        String queryShopId = shopService.nameExist(dto.getShopName());
        String shopId = IdUtil.createSnowflake(productWorker, productDataCenter).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        ShopInfo shopInfo = new ShopInfo()
                .setShopId(shopId)
                .setShopName(dto.getShopName())
                .setGrade(dto.getGrade())
                .setStatus(ShopStatus.VALID.getCode())
                .setIsDel(YesNo.YES.getKey())
                .setCreateBy(executor)
                .setCreateTime(timeMillis)
                .setUpdateBy(executor)
                .setUpdateTime(timeMillis);
        Integer result = shopService.insert(shopInfo);
        return shopId;
    }

    private ShopCarOutput build(ShopInfo shopInfo, Integer sort) {
        return new ShopCarOutput()
                .setShopId(shopInfo.getShopId())
                .setShopName(shopInfo.getShopName())
                .setSort(sort);
    }

    private List<ShopCarOutput.ShopCarProductOutput> build(List<ShopCarDto.ProductDto> productList) {
        List<ShopCarOutput.ShopCarProductOutput> productOutputList = new ArrayList<>();
        for (ShopCarDto.ProductDto productDto : productList) {
            String productId = productDto.getProductId();
            ProductInfo productInfo = productService.getById(productId);
            ProductPriceInfo productPriceInfo = productPriceService.getLastPrice(productId);
            productOutputList.add(
                    new ShopCarOutput.ShopCarProductOutput()
                            .setProductId(productId)
                            .setProductName(productInfo.getName())
                            .setPriceId(productPriceInfo.getPriceId())
                            .setPrice(productPriceInfo.getPrice())
                            .setStock(productDto.getNum()
                            )
            );
        }
        return productOutputList;
    }

    /**
     * @param dtoList     购物车信息
     * @param productInfo 商品信息
     * @param dto         需要购买的商品和数量信息
     * @return 购物车
     * @description: 构建购物车
     */
    private List<ShopCarDto> build(List<ShopCarDto> dtoList, ProductInfo productInfo, ShopCarCreateDto dto) {
        if (CollectionUtil.isEmpty(dtoList)) {
            dtoList = new ArrayList<>();
            List<ShopCarDto.ProductDto> productList = new ArrayList<>();
            productList.add(new ShopCarDto.ProductDto()
                    .setProductId(dto.getProductId())
                    .setNum(dto.getNum())
                    .setSort(CommConstant.ZERO)
            );
            dtoList.add(new ShopCarDto()
                    .setShopId(productInfo.getShopId())
                    .setSort(CommConstant.ZERO)
                    .setProductList(productList)
            );
            return dtoList;
        }
        List<String> shopIdList = dtoList.stream().map(ShopCarDto::getShopId).collect(Collectors.toList());
        boolean flag = shopIdList.contains(productInfo.getShopId());
        if (!flag) {
            List<ShopCarDto.ProductDto> productList = new ArrayList<>();
            productList.add(new ShopCarDto.ProductDto()
                    .setProductId(dto.getProductId())
                    .setNum(dto.getNum())
                    .setSort(CommConstant.ZERO)
            );
            dtoList.add(new ShopCarDto()
                    .setShopId(productInfo.getShopId())
                    .setSort(shopIdList.size() + CommConstant.ONE)
                    .setProductList(productList)
            );
            return dtoList;
        }
        for (ShopCarDto shopCarDto : dtoList) {
            if (!shopCarDto.getShopId().equals(productInfo.getShopId())) {
                continue;
            }
            List<ShopCarDto.ProductDto> productList = shopCarDto.getProductList();
            List<String> productIdList = productList.stream().map(ShopCarDto.ProductDto::getProductId).collect(Collectors.toList());
            if (productIdList.contains(dto.getProductId())) {
                for (ShopCarDto.ProductDto productDto : productList) {
                    if (!productDto.getProductId().equals(dto.getProductId())) {
                        continue;
                    }
                    productDto.setNum(dto.getNum());
                }
            } else {
                productList.add(new ShopCarDto.ProductDto()
                        .setProductId(dto.getProductId())
                        .setNum(dto.getNum())
                        .setSort(productList.size() + CommConstant.ONE)
                );
            }
        }
        return dtoList;
    }

}
