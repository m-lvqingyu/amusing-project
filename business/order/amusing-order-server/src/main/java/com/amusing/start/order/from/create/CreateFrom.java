package com.amusing.start.order.from.create;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单From
 * @date 2021/10/15 16:47
 */
@Data
public class CreateFrom {

    /**
     * 收件人ID
     */
    @NotEmpty(message = "请选择收件人")
    private String consigneeId;

    /**
     * 收件地址
     */
    @NotEmpty(message = "请选择收件地址")
    private String addressId;

}
