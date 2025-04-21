package com.amusing.start.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description: 订单退款信息
 * @since 2023/10/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderAliRefund {

    /**
     * 主键
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 退款流水号
     */
    private String outRequestNo;

    /**
     * 退款金额
     */
    private Integer amount;

    /**
     * 1:初始状态 5:成功 10:失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

}
