package com.amusing.start.order.from;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单From
 * @date 2021/10/15 16:47
 */
@Data
public class OrderCreateFrom {

    /**
     * 商铺ID
     */
    @NotEmpty(message = "商铺信息不存在")
    private String shopsId;

    /**
     * 商品ID
     */
    @NotEmpty(message = "商品信息不存在")
    private String productId;

    /**
     * 价格ID
     */
    @NotEmpty(message = "商品价格不存在")
    private String priceId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    private Integer productNum;

    /**
     * 收件人ID
     */
    @NotEmpty(message = "收件人不能为空")
    private String receiverUserId;

    /**
     * 收件地址
     */
    @NotEmpty(message = "收件地址不能为空")
    private String receiverAddressId;

    /**
     * 优惠券ID
     */
    private String couponId;

    /**
     * 活动ID
     */
    private String activityId;

    /**
     * 设备类型（PC/手机）
     */
    private Integer deviceClass;

    /**
     * 设备版本号
     */
    private String deviceVersion;

    /**
     * 平台类型（浏览器/安卓/IOS）
     */
    private String platformType;

    /**
     * APP版本号
     */
    private String appVersion;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 下单时经纬度
     */
    private String currentPoint;

}
