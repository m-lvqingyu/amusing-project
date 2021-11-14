package com.amusing.start.product.service.impl;

import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.product.enums.MessageStatus;
import com.amusing.start.product.enums.ResultStatus;
import com.amusing.start.product.mapper.ProductInfoMapper;
import com.amusing.start.product.mapper.ProductMessageInfoMapper;
import com.amusing.start.product.mapper.ProductPriceInfoMapper;
import com.amusing.start.product.mapper.ShopInfoMapper;
import com.amusing.start.product.pojo.ProductInfo;
import com.amusing.start.product.pojo.ProductPriceInfo;
import com.amusing.start.product.pojo.ShopInfo;
import com.amusing.start.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    private ShopInfoMapper shopInfoMapper;
    private ProductInfoMapper productInfoMapper;
    private ProductPriceInfoMapper productPriceInfoMapper;
    private ProductMessageInfoMapper productMessageInfoMapper;

    @Autowired
    public ProductServiceImpl(ShopInfoMapper shopInfoMapper,
                              ProductInfoMapper productInfoMapper,
                              ProductPriceInfoMapper productPriceInfoMapper,
                              ProductMessageInfoMapper productMessageInfoMapper) {
        this.shopInfoMapper = shopInfoMapper;
        this.productInfoMapper = productInfoMapper;
        this.productPriceInfoMapper = productPriceInfoMapper;
        this.productMessageInfoMapper = productMessageInfoMapper;
    }

    @Override
    public ProductOutput getProductDetail(String shopId, String productId, String priceId) {
        ShopInfo shopInfo = shopInfoMapper.selectByShopId(shopId);
        if (shopInfo == null) {
            log.warn("[Product]-[getProductDetail]-Shop does not exist!shopId:{}, productId:{}, priceId:{}", shopId, productId, priceId);
            return null;
        }
        ProductInfo productInfo = productInfoMapper.selectByShopAndProductId(shopId, productId);
        if (productInfo == null) {
            log.warn("[Product]-[getProductDetail]-ProductInfo does not exist!shopId:{}, productId:{}, priceId:{}", shopId, productId, priceId);
            return null;
        }
        ProductPriceInfo productPriceInfo = productPriceInfoMapper.selectByProductAndPriceId(productId, priceId);
        if (productPriceInfo == null) {
            log.warn("[Product]-[getProductDetail]-ProductPriceInfo does not exist!shopId:{}, productId:{}, priceId:{}", shopId, productId, priceId);
            return null;
        }
        return build(shopInfo, productInfo, productPriceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deductionProductStock(String txId, String shopId, String productId, Integer productNum) {
        ProductInfo productInfo = productInfoMapper.selectByShopAndProductId(shopId, productId);
        if (productInfo == null) {
            log.warn("[Product]-[deductionProductStock]-ProductInfo does not exist! shopId:{}, productId:{}", shopId, productId);
            return false;
        }
        BigDecimal productStock = productInfo.getProductStock();
        if (productStock == null || productStock.compareTo(BigDecimal.ZERO) < -1) {
            log.warn("[Product]-[deductionProductStock]-ProductInfo insufficient inventory! shopId:{}, productId:{}, productStock:{}", shopId, productId, productStock);
            return false;
        }
        if (new BigDecimal(productNum).compareTo(productStock) < -1) {
            log.warn("[Product]-[deductionProductStock]-ProductInfo insufficient inventory! shopId:{}, productId:{}, productStock:{}, productNum:{}", shopId, productId, productStock, productNum);
            return false;
        }
        int result = productInfoMapper.deductionProductStock(shopId, productId, productNum);
        if (result <= 0) {
            log.warn("[Product]-[deductionProductStock]-ProductInfo insufficient inventory! shopId:{}, productId:{}, productStock:{}, productNum:{}", shopId, productId, productStock, productNum);
            productMessageInfoMapper.updateStatus(txId, MessageStatus.PROCESSED.getCode(), ResultStatus.FAIL.getCode());
            return false;
        }
        productMessageInfoMapper.updateStatus(txId, MessageStatus.PROCESSED.getCode(), ResultStatus.SUCCESS.getCode());
        return true;
    }

    @Override
    public ProductInfo getProductInfo(String shopId, String productId) {
        return productInfoMapper.selectByShopAndProductId(shopId, productId);
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
