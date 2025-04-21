package com.amusing.start.pay.service.ali.h5;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
public interface AliPayH5CloseService {

    /**
     * 关闭订单
     *
     * @param orderNo 订单编号
     * @return true:成功 false:失败
     */
    Boolean close(String orderNo);

}
