package com.amusing.start.pay.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AliPayH5InitRequest {
    /**
     * 订单编号。唯一
     */
    private String orderNo;
    /**
     * 订单标题，不可使用特殊符号
     */
    private String subject;
    /**
     * 支付金额，单位：元。最小值0.01元
     */
    private String amount;
    /**
     * 过期时间。yyyy-MM-dd hh:mm:ss
     */
    private String timeExpire;
    /**
     * 商品列表。非必填
     */
    private List<AliPayH5InitGoodRequest> goodList;
}
