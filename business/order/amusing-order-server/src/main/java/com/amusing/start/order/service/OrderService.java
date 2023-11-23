package com.amusing.start.order.service;

import com.amusing.start.order.entity.dto.CreateDto;
import com.amusing.start.order.entity.pojo.OrderInfo;
import com.amusing.start.order.entity.vo.OrderDetailVo;

/**
 * @author Lv.QingYu
 * @description: 订单Service
 * @since 2021/10/10
 */
public interface OrderService {

    /**
     * @param userId    用户ID
     * @param createDto 订单信息
     * @return 订单编号
     * @description: 创建订单
     */
    String create(String userId, CreateDto createDto);

    /**
     * @param userId  用户ID
     * @param orderNo 订单编号
     * @return 订单详情
     * @description: 获取订单详情
     */
    OrderDetailVo getOrderDetail(String userId, String orderNo);

    /**
     * @param orderNo 订单编号
     * @return 订单信息
     * @description: 根据订单编号，获取订单信息
     */
    OrderInfo getByNo(String orderNo);

    /**
     * @param type                   1:PC网站支付 2:付款码支付 3:扫码支付 4:扫码支付-一码多扫
     * @param notifyId               消息ID
     * @param orderNo                订单编号
     * @param tradeNo                支付宝交易号
     * @param buyerLogonId           买家支付宝账户
     * @param totalAmount            交易金额(单位：分)
     * @param receiptAmount          实收金额(单位：分)
     * @param buyerPayAmount         买家付款金额(单位：分)
     * @param pointAmount            使用集分宝付款的金额(单位：分)
     * @param invoiceAmount          交易中可给用户开具发票的金额
     * @param merchantDiscountAmount 商家优惠金额(单位：分)
     * @param discountAmount         平台优惠金额(单位：分)
     * @param gmtPayment             交易时间
     * @return true:成功  false:失败
     * @description: 支付成功，更新订单状态信息
     */
    Boolean aliPaySuccess(Integer type,
                          String notifyId,
                          String orderNo,
                          String tradeNo,
                          String buyerLogonId,
                          Integer totalAmount,
                          Integer receiptAmount,
                          Integer buyerPayAmount,
                          Integer pointAmount,
                          Integer invoiceAmount,
                          Integer merchantDiscountAmount,
                          Integer discountAmount,
                          String gmtPayment);

}
