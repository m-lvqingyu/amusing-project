package com.amusing.start.order.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class OrderCreateDto {

    private String reserveUserId;

    /**
     * 收件人ID
     */
    private String receiverUserId;

    /**
     * 收件地址
     */
    private String receiverAddressId;

    /**
     * 商品信息集合
     */
    private List<OrderShopDto> shopDtoList;

}
