package com.amusing.start.order.manager;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.exception.OrderException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Administrator
 * @version 1.0
 * @date 2021/12/17 18:27
 */
@Slf4j
@Component
public class InwardManager {

    private final UserClient userClient;
    private final ProductClient productClient;

    public InwardManager(UserClient userClient, ProductClient productClient) {
        this.userClient = userClient;
        this.productClient = productClient;
    }

    /**
     * 获取用户账户信息
     *
     * @param reserveId 预定人ID
     * @return
     * @throws OrderException
     */
    public UserAccountOutput accountDetail(String reserveId) {
        UserAccountOutput userAccountOutput = null;
        try {
            userAccountOutput = userClient.account(reserveId);
        } catch (Exception e) {
            log.error("[order]-create getUserAccountDetails err! reserveUserId:{}, msg:{}", reserveId, Throwables.getStackTraceAsString(e));
        }
        log.info("[order]-create reserveUserId:{}, userAccount:{}", reserveId, userAccountOutput);
        return userAccountOutput;
    }

    /**
     * 获取商品信息
     *
     * @param productIds 商品ID集合
     * @return
     */
    public List<ProductOutput> productDetails(Set<String> productIds) {
        List<ProductOutput> result = null;
        try {
            result = productClient.productDetails(productIds);
        } catch (Exception e) {
            log.error("[order]-create getProductDetails err! param:{}, msg:{}", productIds, Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    public Boolean mainSettlement(String userId, BigDecimal amount) {
        UserSettlementInput input = new UserSettlementInput();
        input.setUserId(userId);
        input.setAmount(amount);
        return userClient.mainSettlement(input);
    }

    public Boolean deductionStock(Map<String, Integer> productMap) {
        List<StockDeductionInput> inputs = new ArrayList<>();
        Iterator<Map.Entry<String, Integer>> iterator = productMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            StockDeductionInput input = StockDeductionInput.builder().productId(next.getKey()).productNum(next.getValue()).build();
            inputs.add(input);
        }
        return productClient.deductionStock(inputs);
    }

    public Map<String, Long> productStock(Set<String> productIds){
        return productClient.productStock(productIds);
    }

}
