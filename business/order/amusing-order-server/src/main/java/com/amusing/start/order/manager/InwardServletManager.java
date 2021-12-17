package com.amusing.start.order.manager;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.client.output.UserAccountOutput;
import com.amusing.start.order.dto.create.OrderShopDto;
import com.amusing.start.order.exception.OrderException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
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
     * @param shopDtoList   商品信息
     * @return
     */
    public List<ShopOutput> getProductDetails(String reserveUserId, List<OrderShopDto> shopDtoList) {
        Set<String> productIdSet = new HashSet<>();
        shopDtoList.forEach(i -> {
            i.getProductDtoList().forEach(x -> {
                productIdSet.add(x.getProductId());
            });
        });

        List<ShopOutput> shopList = null;
        try {
            shopList = productClient.getDetails(productIdSet);
        } catch (Exception e) {
            log.error("[order]-create getProductDetails err! reserveUserId:{}, param:{} , msg:{}",
                    reserveUserId,
                    productIdSet,
                    Throwables.getStackTraceAsString(e));
        }
        return shopList;
    }
}
