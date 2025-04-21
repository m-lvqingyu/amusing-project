package com.amusing.start.order.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.enums.ProductStatus;
import com.amusing.start.order.pojo.Product;
import com.amusing.start.order.pojo.Price;
import com.amusing.start.order.pojo.Shop;
import com.amusing.start.order.req.ApiShoppingReq;
import com.amusing.start.order.resp.ApiShopCarDetailResp;
import com.amusing.start.order.resp.ApiShopCarResp;
import com.amusing.start.order.service.PriceService;
import com.amusing.start.order.service.ProductService;
import com.amusing.start.order.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @since 2025/3/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShopCarBiz {

    private final RedissonClient redissonClient;

    private final ShopService shopService;

    private final ProductService productService;

    private final PriceService productPriceService;

    private static final String SHOP_CAR_CACHE_PREFIX = "sh:car:us:";

    private static final long SHOP_CAR_CACHE_TIME_OUT = 7;

    public Boolean shopping(String userId, ApiShoppingReq req) {
        Product product = productService.getById(req.getProductId());
        if (product == null || !product.getStatus().equals(ProductStatus.VALID.getCode())) {
            throw new CustomException(OrderErrorCode.PRODUCT_NOT_FOUND);
        }
        String cacheKey = getShopCarCacheKey(userId);
        RBucket<List<ApiShopCarResp>> bucket = redissonClient.getBucket(cacheKey);
        List<ApiShopCarResp> responseList = bucket.get();
        responseList = build(responseList, product, req);
        bucket.set(responseList, SHOP_CAR_CACHE_TIME_OUT, TimeUnit.DAYS);
        return Boolean.TRUE;
    }

    public List<ApiShopCarDetailResp> shopCar(String userId) {
        String cacheKey = getShopCarCacheKey(userId);
        RBucket<List<ApiShopCarResp>> bucket = redissonClient.getBucket(cacheKey);
        List<ApiShopCarResp> shopCarDtoList = bucket.get();
        if (CollectionUtil.isEmpty(shopCarDtoList)) {
            return new ArrayList<>();
        }
        List<ApiShopCarDetailResp> responseList = new ArrayList<>();
        for (ApiShopCarResp shopCarDto : shopCarDtoList) {
            String shopId = shopCarDto.getShopId();
            Shop shop = shopService.getById(shopId);
            ApiShopCarDetailResp response = new ApiShopCarDetailResp()
                    .setShopId(shop.getId())
                    .setShopName(shop.getName())
                    .setSort(shopCarDto.getSort());
            List<ApiShopCarDetailResp.ApiProductDetailResp> productDetailList = new ArrayList<>();
            for (ApiShopCarResp.ApiProductResp productResponse : shopCarDto.getProductList()) {
                String productId = productResponse.getProductId();
                Product product = productService.getById(productId);
                Price productPrice = productPriceService.getLastPrice(productId);
                productDetailList.add(
                        new ApiShopCarDetailResp.ApiProductDetailResp()
                                .setProductId(productId)
                                .setProductName(product.getName())
                                .setPriceId(productPrice.getPriceId())
                                .setPrice(productPrice.getPrice())
                                .setStock(productResponse.getNum()));
            }
            response.setProductDetailList(productDetailList);
            responseList.add(response);
        }
        return responseList;
    }


    /**
     * @param respList 购物车信息
     * @param product  商品信息
     * @param req      需要购买的商品和数量信息
     * @return 购物车
     * @description: 构建购物车
     */
    private List<ApiShopCarResp> build(List<ApiShopCarResp> respList, Product product, ApiShoppingReq req) {
        if (CollectionUtil.isEmpty(respList)) {
            respList = new ArrayList<>();
            List<ApiShopCarResp.ApiProductResp> productList = new ArrayList<>();
            productList.add(new ApiShopCarResp.ApiProductResp()
                    .setProductId(req.getProductId())
                    .setNum(req.getNum())
                    .setSort(CommConstant.ZERO)
            );
            respList.add(new ApiShopCarResp()
                    .setShopId(product.getShopId())
                    .setSort(CommConstant.ZERO)
                    .setProductList(productList)
            );
            return respList;
        }
        List<String> shopIdList = respList.stream().map(ApiShopCarResp::getShopId).collect(Collectors.toList());
        boolean flag = shopIdList.contains(product.getShopId());
        if (!flag) {
            List<ApiShopCarResp.ApiProductResp> productList = new ArrayList<>();
            productList.add(new ApiShopCarResp.ApiProductResp()
                    .setProductId(req.getProductId())
                    .setNum(req.getNum())
                    .setSort(CommConstant.ZERO)
            );
            respList.add(new ApiShopCarResp()
                    .setShopId(product.getShopId())
                    .setSort(shopIdList.size() + CommConstant.ONE)
                    .setProductList(productList)
            );
            return respList;
        }
        for (ApiShopCarResp resp : respList) {
            if (!resp.getShopId().equals(product.getShopId())) {
                continue;
            }
            List<ApiShopCarResp.ApiProductResp> productList = resp.getProductList();
            List<String> productIdList = productList.stream().map(ApiShopCarResp.ApiProductResp::getProductId).collect(Collectors.toList());
            if (productIdList.contains(req.getProductId())) {
                for (ApiShopCarResp.ApiProductResp productResponse : productList) {
                    if (!productResponse.getProductId().equals(req.getProductId())) {
                        continue;
                    }
                    productResponse.setNum(req.getNum());
                }
            } else {
                productList.add(new ApiShopCarResp.ApiProductResp()
                        .setProductId(req.getProductId())
                        .setNum(req.getNum())
                        .setSort(productList.size() + CommConstant.ONE)
                );
            }
        }
        return respList;
    }

    private String getShopCarCacheKey(String userId) {
        return SHOP_CAR_CACHE_PREFIX + userId;
    }
}
