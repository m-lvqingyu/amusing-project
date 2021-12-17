package com.amusing.start.order.from.create;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单From
 * @date 2021/10/15 16:47
 */
@Data
public class OrderCreateFrom {

    /**
     * 收件人ID
     */
    @NotEmpty(message = "请选择收件人")
    private String receiverUserId;

    /**
     * 收件地址
     */
    @NotEmpty(message = "请选择收件地址")
    private String receiverAddressId;

    /**
     * 商品信息集合
     */
    @NotNull(message = "请选择需要购买的商品")
    private List<OrderShopFrom> shopFromList;

}
