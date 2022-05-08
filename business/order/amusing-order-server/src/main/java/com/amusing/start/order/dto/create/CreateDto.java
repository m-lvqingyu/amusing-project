package com.amusing.start.order.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单From
 * @date 2021/10/15 16:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDto {

    /**
     * 创建人ID
     */
    private String reserveId;

    /**
     * 收件人ID
     */
    private String consigneeId;

    /**
     * 收件地址
     */
    private String addressId;

}
