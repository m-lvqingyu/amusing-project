package com.amusing.start.client.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderDetailOutput {

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 预定人ID
     */
    private String reserveId;

    /**
     * 收件人ID
     */
    private String consigneeId;

    /**
     * 订单实际金额
     */
    private Integer realAmount;

    /**
     * 描述
     */
    private String describe;

}
