package com.amusing.start.order.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResp {
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 预定人ID
     */
    private String userId;
    /**
     * 收件人姓名
     */
    private String name;
    /**
     * 收件人手机号
     */
    private String phone;
    /**
     * 收件地址-省编码
     */
    private String provinces;
    /**
     * 收件地址-市编码
     */
    private String cities;
    /**
     * 收件地址-区编码
     */
    private String districts;
    /**
     * 地址详情
     */
    private String address;
    /**
     * 订单总金额(单位：分)
     */
    private Integer totalAmount;
    /**
     * 订单实际金额(单位：分)
     */
    private Integer realAmount;
    /**
     * 优惠减免金额(单位：分)
     */
    private Integer reductionAmount;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 支付类型 1:支付宝 2:微信
     */
    private Integer payType;
    /**
     * 支付ID
     */
    private Long payId;
    /**
     * 是否能够退款(1:可退款 2:不可退款)
     */
    private Integer canRefund;
    /**
     * 商品信息
     */
    private List<OrderShopsResponse> shopsResponseList;

}
