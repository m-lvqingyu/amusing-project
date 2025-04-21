package com.amusing.start.pay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5RefundGoodRequest {
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 退款金额
     */
    private String refundAmount;
}
