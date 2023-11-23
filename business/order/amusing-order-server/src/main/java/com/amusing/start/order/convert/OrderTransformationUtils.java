package com.amusing.start.order.convert;

import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.order.entity.pojo.OrderInfo;
import com.amusing.start.order.entity.pojo.OrderProductInfo;
import com.amusing.start.order.entity.pojo.OrderShopsInfo;
import com.amusing.start.order.enums.*;
import org.springframework.stereotype.Component;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/20
 */
@Component
public class OrderTransformationUtils {

    public OrderShopsInfo beanTransformation(ShopCarOutput output, String orderNo) {
        return new OrderShopsInfo()
                .setShopsId(output.getShopId())
                .setShopsName(output.getShopName())
                .setOrderNo(orderNo)
                .setSort(output.getSort());
    }

    public OrderProductInfo beanTransformation(String orderNo,
                                                      Integer amount,
                                                      String shopId,
                                                      ShopCarOutput.ShopCarProductOutput productOutput) {
        return new OrderProductInfo()
                .setOrderNo(orderNo)
                .setShopId(shopId)
                .setProductId(productOutput.getProductId())
                .setProductName(productOutput.getProductName())
                .setPriceId(productOutput.getPriceId())
                .setPrice(productOutput.getPrice())
                .setNum(productOutput.getStock())
                .setAmount(amount);
    }

    public StockDeductionInput beanTransformation(ShopCarOutput.ShopCarProductOutput productOutput) {
        return new StockDeductionInput()
                .setProductId(productOutput.getProductId())
                .setProductNum(productOutput.getStock());
    }

    public OrderInfo buildDefOrder() {
        Long currentTime = System.currentTimeMillis();
        return new OrderInfo().setUseCoupon(OrderUseCouponEnum.NO.getKey())
                .setCouponAmount(CommConstant.ZERO)
                .setUseActivity(OrderUseActivityEnum.NO.getKey())
                .setActivityAmount(CommConstant.ZERO)
                .setIsFreight(OrderFreightEnum.YES.getKey())
                .setFreightAmount(CommConstant.ZERO)
                .setStatus(OrderStatusEnum.SCHEDULED.getKey())
                .setIsEvaluate(OrderEvaluateEnum.NO.getKey())
                .setCanRefund(OrderCanRefundEnum.YES.getKey())
                .setRefundAmount(CommConstant.ZERO)
                .setCreateTime(currentTime)
                .setUpdateTime(currentTime);
    }

}
