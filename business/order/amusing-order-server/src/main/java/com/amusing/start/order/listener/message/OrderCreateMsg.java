package com.amusing.start.order.listener.message;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
@Builder
public class OrderCreateMsg {

    private String userId;

    private String shopsId;

    private String productId;

    private Integer productNum;

    private String priceId;

    private BigDecimal amount;

    private String orderNo;

}