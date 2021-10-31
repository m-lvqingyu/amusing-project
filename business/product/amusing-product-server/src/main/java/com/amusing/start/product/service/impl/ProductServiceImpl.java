package com.amusing.start.product.service.impl;

import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.mapper.ProductPriceInfoMapper;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.pojo.ProductInfo;
import com.amusing.start.product.pojo.ProductPriceInfo;
import com.amusing.start.product.pojo.ShopInfo;
import com.amusing.start.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private ShopInfoMapper shopInfoMapper;
    private ProductInfoMapper productInfoMapper;
    private ProductPriceInfoMapper productPriceInfoMapper;

    @Autowired
    public ProductServiceImpl(ShopInfoMapper shopInfoMapper, ProductInfoMapper productInfoMapper, ProductPriceInfoMapper productPriceInfoMapper) {
        this.shopInfoMapper = shopInfoMapper;
        this.productInfoMapper = productInfoMapper;
        this.productPriceInfoMapper = productPriceInfoMapper;
    }

    @Override
    public ProductOutput getProductDetail(String shopId, String productId, String priceId) {
        ShopInfo shopInfo = shopInfoMapper.selectByShopId(shopId);
        if (shopInfo == null) {
            log.warn("[Product]-[getProductDetail]-Shop does not exist!shopId:{}, productId:{}, priceId:{}", shopId, priceId, priceId);
            return null;
        }
        ProductInfo productInfo = productInfoMapper.selectByShopAndProductId(shopId, productId);
        if (productInfo == null) {
            log.warn("[Product]-[getProductDetail]-ProductInfo does not exist!shopId:{}, productId:{}, priceId:{}", shopId, priceId, priceId);
            return null;
        }
        ProductPriceInfo productPriceInfo = productPriceInfoMapper.selectByProductAndPriceId(productId, priceId);
        if (productPriceInfo == null) {
            log.warn("[Product]-[getProductDetail]-ProductPriceInfo does not exist!shopId:{}, productId:{}, priceId:{}", shopId, priceId, priceId);
            return null;
        }
        return build(shopInfo, productInfo, productPriceInfo);
    }

    private ProductOutput build(ShopInfo shopInfo, ProductInfo productInfo, ProductPriceInfo productPriceInfo) {
        ProductOutput output = new ProductOutput();
        BeanUtils.copyProperties(productInfo, output);
        output.setShopId(shopInfo.getShopId());
        output.setShopName(shopInfo.getShopName());
        output.setPrice(productPriceInfo.getPrice());
        output.setPriceId(productPriceInfo.getPriceId());
        return output;
    }
}
