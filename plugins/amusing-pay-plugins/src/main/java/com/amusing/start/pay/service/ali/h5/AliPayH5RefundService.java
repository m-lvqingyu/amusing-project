package com.amusing.start.pay.service.ali.h5;

import com.amusing.start.pay.request.AliPayH5RefundRequest;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
public interface AliPayH5RefundService {

    /**
     * 退款
     *
     * @param request 请求参数
     * @return true:成功 false:失败
     */
    Boolean refund(AliPayH5RefundRequest request);

}
