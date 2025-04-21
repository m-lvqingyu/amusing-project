package com.amusing.start.pay.service.ali.h5;

import com.amusing.start.pay.response.AliPayH5RefundQueryResponse;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
public interface AliPayH5RefundQueryService {

    /**
     * 退款查询（如果退款查询发起时间早于退款时间，或者间隔退款发起时间太短，可能出现退款查询时还没处理成功，后面又处理成功的情况。
     * 建议商户在退款发起后间隔10秒以上再发起退款查询请求。）
     *
     * @param orderNo      订单编号
     * @param outRequestNo 退款编号
     * @return 退款信息
     */
    AliPayH5RefundQueryResponse refundQuery(String orderNo, String outRequestNo);
    
}
