package com.amusing.start.platform.service.pay.ali;

import com.amusing.start.client.output.pay.ali.BarCodePayOutput;
import com.amusing.start.client.output.pay.ali.CancelOutput;

/**
 * @author Lv.QingYu
 * @description: 支付宝-付款码支付
 * @link <a href="https://opendocs.alipay.com/open/1f1fe18c_alipay.trade.pay?pathHash=29c9a9ba&ref=api&scene=32">...</a>
 * @since 2023/11/9
 */
public interface BarCodePayService {

    /**
     * @param orderNo 订单编号
     * @return 撤销结果
     * @description: 没有明确的支付结果时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；如果用户支付成功，支付宝系统会将此订单资金退还给用户
     */
    CancelOutput cancel(String orderNo);
}
