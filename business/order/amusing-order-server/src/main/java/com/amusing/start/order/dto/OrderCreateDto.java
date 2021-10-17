package com.amusing.start.order.dto;

import lombok.Data;

/**
 * @author lv.QingYu
 * @version 1.0
 * @description: 创建订单Dto
 * @date 2021/10/15 17:27
 */
@Data
public class OrderCreateDto {

    /**
     * 预定人ID
     */
    private String reserveUserId;

    /**
     * 商铺ID
     */
    private String shopsId;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer productNum;

    /**
     * 收件人ID
     */
    private String receiverUserId;

    /**
     * 收件地址
     */
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
