package com.amusing.start.platform.service.pay.ali;

import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayPreCreateVo;

/**
 * @author Lv.QingYu
 * @description: 支付宝-扫码支付
 * @since 2023/11/9
 */
public interface ScanCodePayService {


    /**
     * @param orderNo
     * @param amount
     * @param subject
     * @param buyerId
     * @param timeoutExpress
     * @return
     * @description: 统一收单交易创建接口:一般用于一码多付场景，如商家生成一个固定二维码，用户钱包扫码跳转输入支付金额创建订单支付；
     */
    Boolean create(String orderNo, Integer amount, String subject, String buyerId, String timeoutExpress);


}
