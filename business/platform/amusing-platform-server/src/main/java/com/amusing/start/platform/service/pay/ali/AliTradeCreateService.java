package com.amusing.start.platform.service.pay.ali;

import com.amusing.start.client.output.pay.ali.BarCodePayOutput;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayCreateVo;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayPreCreateVo;

/**
 * @author Lv.QingYu
 * @description: 支付宝 - 统一收单下单并支付页面接口
 * @since 2023/11/10
 */
public interface AliTradeCreateService {

    /**
     * @param orderNo 订单编号
     * @return 下单结果
     * @description: 电脑网站支付:PC场景下单并支付
     */
    String payFormPc(String orderNo);

    /**
     * @param orderNo  订单编号
     * @param authCode 用户付款码
     * @return 支付结果
     * @description: 付款码支付：收银员使用扫码设备读取用户手机支付宝“付款码”获取设备（如扫码枪）读取用户手机支付宝的付款码信息后，
     * 将二维码或条码信息通过本接口上送至支付宝发起支付
     */
    BarCodePayOutput payFormBarCode(String orderNo, String authCode);

    /**
     * @param orderNo 订单编号
     * @return 结果
     * @description: 扫码支付:商家生成一个动态的二维码，用户钱包扫码支付；
     */
    ScanCodePayPreCreateVo payFormPreScanCode(String orderNo);

    /**
     * @param orderNo 订单编号
     * @param buyerId 买家支付宝用户ID
     * @return 扫码信息
     * @description: 扫码支付:商家生成一个动态的二维码，用户钱包扫码支付；
     */
    ScanCodePayCreateVo payFormScanCode(String orderNo, String buyerId);
    
}
