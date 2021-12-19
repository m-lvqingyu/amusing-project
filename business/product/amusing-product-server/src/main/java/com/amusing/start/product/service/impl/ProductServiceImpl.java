package com.amusing.start.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.code.CommCode;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ProductServiceImpl(ProductInfoMapper productInfoMapper,
                              ShopInfoMapper shopInfoMapper,
                              ProductPriceInfoMapper productPriceInfoMapper) {
        this.productInfoMapper = productInfoMapper;
        this.shopInfoMapper = shopInfoMapper;
        this.productPriceInfoMapper = productPriceInfoMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deductionStock(List<StockDeductionInput> inputs) throws ProductException {
        Integer result = null;
        try {
            result = productInfoMapper.batchDeductionStock(inputs);
        } catch (Exception e) {
            log.error("[product]-batchDeductionStock err! param:{}, msg:{}", inputs, Throwables.getStackTraceAsString(e));
        }
        Optional.ofNullable(result).filter(i -> i == inputs.size()).orElseThrow(() -> new ProductException(CommCode.FAIL));
        return true;
    }

    @Override
    public List<ShopOutput> getDetails(Set<String> productIds) {
        List<ShopOutput> result = new ArrayList<>();

        List<ProductInfo> productInfoList = productInfoMapper.getDetailsByIds(productIds);
        for (ProductInfo productInfo : productInfoList) {
            String shopId = productInfo.getShopId();
            ProductOutput productOutput = ProductOutput.builder()
                    .shopId(shopId)
                    .productId(productInfo.getProductId())
                    .productName(productInfo.getProductName())
                    .productStock(productInfo.getProductStock())
                    .build();
            if (CollectionUtil.isEmpty(result)) {
                createShopOutput(shopId, productOutput, result);
                continue;
            }

            for (ShopOutput shopOutput : result) {
                String outputShopId = shopOutput.getShopId();
                if (outputShopId.equals(shopId)) {
                    List<ProductOutput> productList = shopOutput.getProductList();
                    ProductPriceInfo priceInfo = productPriceInfoMapper.selectLastRecordByProductId(productOutput.getProductId());
                    productOutput.setPriceId(priceInfo.getPriceId());
                    productOutput.setPrice(priceInfo.getPrice());
                    productList.add(productOutput);
                } else {
                    createShopOutput(shopId, productOutput, result);
                }
            }
        }

        return result;
    }

    private void createShopOutput(String shopId, ProductOutput productOutput, List<ShopOutput> result) {
        ProductPriceInfo priceInfo = productPriceInfoMapper.selectLastRecordByProductId(productOutput.getProductId());
        ShopInfo shopInfo = shopInfoMapper.selectByShopId(shopId);
        ShopOutput shopOutput = new ShopOutput();
        shopOutput.setShopId(shopId);
        shopOutput.setShopName(shopInfo.getShopName());
        shopOutput.setGrade(shopInfo.getGrade());
        shopOutput.setStatus(shopInfo.getStatus());
        List<ProductOutput> productOutputs = new ArrayList<>();
        productOutput.setPriceId(priceInfo.getPriceId());
        productOutput.setPrice(priceInfo.getPrice());
        productOutputs.add(productOutput);
        shopOutput.setProductList(productOutputs);
        result.add(shopOutput);
    }


}
