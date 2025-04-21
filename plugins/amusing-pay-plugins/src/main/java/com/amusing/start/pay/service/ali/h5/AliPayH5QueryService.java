package com.amusing.start.pay.service.ali.h5;

import com.amusing.start.pay.response.AliPayH5QueryResponse;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
public interface AliPayH5QueryService {

    /**
     * 查询订单支付详情
     *
     * @param orderNo 订单编号
     * @return 详情
     */
    AliPayH5QueryResponse query(String orderNo);

}
