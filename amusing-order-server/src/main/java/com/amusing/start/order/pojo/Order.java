package com.amusing.start.order.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Lv.QingYu
 * @since 2021/10/10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
public class Order {
    /**
     * 订单编号
     */
    @TableId(type = IdType.INPUT)
    private String orderNo;
    /**
     * 预定人ID
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String userId;
    /**
     * 收件人姓名
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String name;
    /**
     * 收件人手机号
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String phone;
    /**
     * 收件地址-省编码
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String provinces;
    /**
     * 收件地址-市编码
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String cities;
    /**
     * 收件地址-区编码
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String districts;
    /**
     * 地址详情
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String address;
    /**
     * 订单总金额(单位：分)
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer totalAmount;
    /**
     * 订单实际金额(单位：分)
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer realAmount;
    /**
     * 优惠减免金额(单位：分)
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer reductionAmount;
    /**
     * 订单状态
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer status;
    /**
     * 支付类型 1:支付宝 2:微信
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer payType;
    /**
     * 支付ID
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Long payId;
    /**
     * 是否能够退款(1:可退款 2:不可退款)
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer canRefund;
    /**
     * 已退款金额
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Integer refundAmount;
    /**
     * 创建人
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String createBy;
    /**
     * 更新人
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private String updateBy;
    /**
     * 创建时间
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Long createTime;
    /**
     * 更新时间
     */
    @TableField(insertStrategy = FieldStrategy.NOT_NULL)
    private Long updateTime;

}