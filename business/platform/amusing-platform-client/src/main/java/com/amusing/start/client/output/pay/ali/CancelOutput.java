package com.amusing.start.client.output.pay.ali;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CancelOutput {

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 是否需要重试
     */
    private Boolean retryFlag;

    /**
     * close：交易未支付，触发关闭交易动作，无退款；
     * refund：交易已支付，触发交易退款动作；
     * 未返回：未查询到交易，或接口调用失败；
     */
    private String action;
    
}
