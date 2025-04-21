package com.amusing.start.pay.service.ali.h5.impl;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.pay.enums.AliPayRefundStatus;
import com.amusing.start.pay.response.AliPayH5RefundQueryResponse;
import com.amusing.start.pay.service.ali.h5.AliPayH5RefundQueryService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Slf4j
public class AliPayH5RefundQueryServiceImpl implements AliPayH5RefundQueryService {

    private final AlipayClient alipayClient;

    public AliPayH5RefundQueryServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    /**
     * refund_detail_item_list：本次退款使用的资金渠道； gmt_refund_pay：退款执行成功的时间； deposit_back_info：银行卡冲退信息
     */
    private static final String DEF_QUERY_OPTIONS = "refund_detail_item_list";

    @Override
    public AliPayH5RefundQueryResponse refundQuery(String orderNo, String outRequestNo) {
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(orderNo);
        List<String> queryOptions = new ArrayList<>();
        queryOptions.add(DEF_QUERY_OPTIONS);
        model.setQueryOptions(queryOptions);
        model.setOutRequestNo(outRequestNo);

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizModel(model);
        AlipayTradeFastpayRefundQueryResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            log.error("[AliPay]-refundQuery err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        log.info("[AliPay]-refundQuery result:{}", JSONUtil.toJsonStr(response));
        return new AliPayH5RefundQueryResponse().setAliOrderNo(response.getTradeNo())
                .setOrderNo(orderNo)
                .setOutRequestNo(outRequestNo)
                .setTotalAmount(response.getTotalAmount())
                .setRefundAmount(response.getRefundAmount())
                .setStatus(AliPayRefundStatus.valueOf(response.getRefundStatus()).getKey());
    }

}
