package com.amusing.start.order.manager;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.order.pojo.OrderProductInfo;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/17 18:27
 */
@Slf4j
@Component
public class InwardServletManager {

    private final UserClient userClient;
    private final ProductClient productClient;

    public InwardServletManager(UserClient userClient, ProductClient productClient) {
        this.userClient = userClient;
        this.productClient = productClient;
    }

    /**
     * 获取用户账户信息
     *
     * @param reserveUserId 预定人ID
     * @return
     * @throws OrderException
     */
    public UserAccountOutput getUserAccountDetails(String reserveUserId) {
        UserAccountOutput userAccountOutput = null;
        try {
            userAccountOutput = userClient.account(reserveUserId);
        } catch (Exception e) {
            log.error("[order]-create getUserAccountDetails err! reserveUserId:{}, msg:{}", reserveUserId, Throwables.getStackTraceAsString(e));
        }
        log.info("[order]-create reserveUserId:{}, userAccount:{}", reserveUserId, userAccountOutput);
        return userAccountOutput;
    }

    /**
     * 获取商品信息
     *
     * @param reserveUserId 预定人ID
     * @param productIds    商品ID集合
     * @return
     */
    public Map<String, ProductOutput> getProductDetails(String reserveUserId, Set<String> productIds) {
        Map<String, ProductOutput> result = null;
        try {
            result = productClient.getProductDetails(productIds);
        } catch (Exception e) {
            log.error("[order]-create getProductDetails err! reserveUserId:{}, param:{} , msg:{}", reserveUserId, productIds, Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    /**
     * 获取商铺信息集合
     *
     * @param reserveUserId 预定人ID
     * @param shopIds       商铺ID集合
     * @return
     */
    public Map<String, ShopOutput> getShopDetails(String reserveUserId, Set<String> shopIds) {
        Map<String, ShopOutput> result = null;
        try {
            result = productClient.getShopDetails(shopIds);
        } catch (Exception e) {
            log.error("[order]-create getShopDetails err! reserveUserId:{}, param:{} , msg:{}", reserveUserId, shopIds, Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    public boolean mainSettlement(String userId, BigDecimal amount) {
        UserSettlementInput input = new UserSettlementInput();
        input.setUserId(userId);
        input.setAmount(amount);
        return userClient.userMainSettlement(input);
    }

    public boolean deductionStock(List<OrderProductInfo> productInfoList) {
        List<StockDeductionInput> inputs = new ArrayList<>();
        productInfoList.forEach(i -> {
            inputs.add(new StockDeductionInput(i.getProductId(), i.getProductNum()));
        });
        return productClient.deductionStock(inputs);
    }
}
