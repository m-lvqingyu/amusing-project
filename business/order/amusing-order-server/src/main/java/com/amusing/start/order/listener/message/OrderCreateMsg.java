package com.amusing.start.order.listener.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateMsg implements Serializable {
    
    private static final long serialVersionUID = 8221925920123045843L;

    private String userId;

    private String shopsId;

    private String productId;

    private Integer productNum;

    private String priceId;

    private BigDecimal amount;

    private String orderNo;

}
