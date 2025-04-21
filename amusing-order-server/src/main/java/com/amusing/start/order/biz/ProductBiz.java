package com.amusing.start.order.biz;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.order.config.props.SnowflakeProps;
import com.amusing.start.order.enums.OrderErrorCode;
import com.amusing.start.order.enums.ProductStatus;
import com.amusing.start.order.enums.YesNo;
import com.amusing.start.order.pojo.Price;
import com.amusing.start.order.pojo.Product;
import com.amusing.start.order.req.AdminProductCreateReq;
import com.amusing.start.order.service.PriceService;
import com.amusing.start.order.service.ProductService;
import com.amusing.start.order.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Lv.QingYu
 * @since 2025/3/5
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ProductBiz {

    private final ShopService shopService;

    private final ProductService productService;

    private final PriceService productPriceService;

    private final SnowflakeProps props;

    @Transactional(rollbackFor = Exception.class)
    public void create(String userId, AdminProductCreateReq req) {
        if (shopService.getById(req.getShopId()) == null) {
            throw new CustomException(OrderErrorCode.SHOP_NOT_FOUND);
        }
        if (productService.getByName(req.getShopId(), req.getName()) != null) {
            throw new CustomException(OrderErrorCode.PRODUCT_NAME_EXIST);
        }
        String productId = IdUtil.createSnowflake(props.getWorker(), props.getCenter()).nextIdStr();
        long timeMillis = System.currentTimeMillis();
        Product productInfo = new Product().setShopId(req.getShopId())
                .setId(productId)
                .setName(req.getName())
                .setStock(req.getStock())
                .setStatus(ProductStatus.VALID.getCode())
                .setIsDel(YesNo.YES.getKey())
                .setCreateBy(userId)
                .setCreateTime(timeMillis)
                .setUpdateBy(userId)
                .setUpdateTime(timeMillis);
        productService.insert(productInfo);
        // 保存商品价格
        String priceId = IdUtil.createSnowflake(props.getWorker(), props.getCenter()).nextIdStr();
        Price priceInfo = new Price()
                .setId(priceId)
                .setProductId(productId)
                .setVersion(CommConstant.ZERO)
                .setPrice(req.getPrice())
                .setCreateBy(userId)
                .setCreateTime(timeMillis)
                .setUpdateBy(userId)
                .setUpdateTime(timeMillis);
        productPriceService.insert(priceInfo);
    }

}
