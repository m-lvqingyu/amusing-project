package com.amusing.start.product.listener.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Create By 2021/11/14
 *
 * @author lvqingyu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReduceStockMsg implements Serializable {

    private static final long serialVersionUID = 8221925920123045843L;

    private String shopsId;

    private String productId;

    private Integer productNum;

    private String priceId;

    private BigDecimal amount;

}
