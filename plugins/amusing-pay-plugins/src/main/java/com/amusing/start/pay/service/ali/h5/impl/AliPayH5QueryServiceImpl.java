package com.amusing.start.pay.service.ali.h5.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.pay.enums.AliPayStatus;
import com.amusing.start.pay.response.AliPayH5QueryGoodResponse;
import com.amusing.start.pay.response.AliPayH5QueryResponse;
import com.amusing.start.pay.service.ali.h5.AliPayH5QueryService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/8
 */
@Slf4j
public class AliPayH5QueryServiceImpl implements AliPayH5QueryService {

    private final AlipayClient alipayClient;

    public AliPayH5QueryServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    /**
     * 交易结算信息: trade_settle_info
     * 交易支付使用的资金渠道: fund_bill_list
     * 交易支付时使用的所有优惠券信息: voucher_detail_list
     */
    private static final String DEF_QUERY_OPTIONS = "trade_settle_info";

    @Override
    public AliPayH5QueryResponse query(String orderNo) {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderNo);
        List<String> queryOptions = new ArrayList<String>();
        queryOptions.add(DEF_QUERY_OPTIONS);
        model.setQueryOptions(queryOptions);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        AlipayTradeQueryResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            log.error("[AliPay]-query err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        List<GoodsDetail> reqGoodsDetail = response.getReqGoodsDetail();
        List<AliPayH5QueryGoodResponse> initGoodResponse = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reqGoodsDetail)) {
            for (GoodsDetail goodsDetail : reqGoodsDetail) {
                initGoodResponse.add(new AliPayH5QueryGoodResponse()
                        .setGoodsId(goodsDetail.getGoodsId())
                        .setGoodsName(goodsDetail.getGoodsName())
                        .setQuantity(goodsDetail.getQuantity())
                        .setPrice(goodsDetail.getPrice())
                        .setBody(goodsDetail.getBody()));
            }
        }
        return new AliPayH5QueryResponse().setAliOrderNo(response.getTradeNo())
                .setOrderNo(orderNo)
                .setBuyerLogonId(response.getBuyerLogonId())
                .setStatus(AliPayStatus.getByValue(response.getTradeStatus()).getKey())
                .setTotalAmount(response.getTotalAmount())
                .setPeriodScene(response.getPeriodScene())
                .setTransCurrency(response.getTransCurrency())
                .setSettleCurrency(response.getSettleCurrency())
                .setSettleAmount(response.getSettleAmount())
                .setPayCurrency(response.getPayCurrency())
                .setPayAmount(response.getPayAmount())
                .setSettleTransRate(response.getSettleTransRate())
                .setTransPayRate(response.getTransPayRate())
                .setBuyerPayAmount(response.getBuyerPayAmount())
                .setPointAmount(response.getPointAmount())
                .setInvoiceAmount(response.getInvoiceAmount())
                .setSendPayDate(response.getSendPayDate())
                .setReceiptAmount(response.getReceiptAmount())
                .setBuyerOpenId(response.getBuyerOpenId())
                .setInitGoodResponse(initGoodResponse);
    }

}
