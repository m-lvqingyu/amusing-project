package com.amusing.start.order.manager;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.input.UserSettlementInput;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.enums.OrderCode;
import com.amusing.start.order.exception.OrderException;
import com.amusing.start.result.ApiResult;
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
     * @return 用户信息
     * @throws OrderException
     */
    public UserAccountOutput accountDetail(String reserveId) throws OrderException {
        ApiResult<UserAccountOutput> result = userClient.account(reserveId);
        if (!result.isSuccess()) {
            throw new OrderException(OrderCode.USER_NOT_FOUND);
        }
        return result.getData();
    }

    /**
     * 主账户结算
     *
     * @param userId 用户ID
     * @param amount 结算金额
     * @return true:成功 false:失败
     */
    public Boolean mainSettlement(String userId, BigDecimal amount) throws OrderException {
        UserSettlementInput input = new UserSettlementInput();
        input.setUserId(userId);
        input.setAmount(amount);
        ApiResult<Boolean> result = userClient.mainSettlement(input);
        if (!result.isSuccess()) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        return result.getData();
    }

    /**
     * 获取商品信息
     *
     * @param productIds 商品ID集合
     * @return 商品信息集合
     */
    public List<ProductOutput> productDetails(Set<String> productIds) throws OrderException {
        ApiResult<List<ProductOutput>> result = productClient.productDetails(productIds);
        if (!result.isSuccess()) {
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        return result.getData();
    }

    /**
     * 扣减库存
     *
     * @param productMap 购物车信息
     * @return true:成功 false:失败
     * @throws OrderException
     */
    public Boolean deductionStock(Map<String, Integer> productMap) throws OrderException {
        List<StockDeductionInput> inputs = new ArrayList<>();
        Iterator<Map.Entry<String, Integer>> iterator = productMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            StockDeductionInput input = StockDeductionInput.builder().productId(next.getKey()).productNum(next.getValue()).build();
            inputs.add(input);
        }
        ApiResult<Boolean> result = productClient.deductionStock(inputs);
        if (!result.isSuccess()) {
            throw new OrderException(OrderCode.UNABLE_PROVIDE_SERVICE);
        }
        return result.getData();
    }

    /**
     * 获取商品库存信息
     *
     * @param productIds 商品ID集合
     * @return 库存信息集合
     * @throws OrderException
     */
    public Map<String, Long> productStock(Set<String> productIds) throws OrderException {
        ApiResult<Map<String, Long>> result = productClient.productStock(productIds);
        if (!result.isSuccess()) {
            throw new OrderException(OrderCode.PRODUCT_NOT_FOUND);
        }
        return result.getData();
    }

}
