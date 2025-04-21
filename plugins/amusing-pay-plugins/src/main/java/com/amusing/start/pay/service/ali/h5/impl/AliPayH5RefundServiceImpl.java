package com.amusing.start.pay.service.ali.h5.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.RefundGoodsDetail;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.pay.request.AliPayH5RefundGoodRequest;
import com.amusing.start.pay.request.AliPayH5RefundRequest;
import com.amusing.start.pay.service.ali.h5.AliPayH5RefundService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Slf4j
public class AliPayH5RefundServiceImpl implements AliPayH5RefundService {

    private final AlipayClient alipayClient;

    public AliPayH5RefundServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    private static final String DEF_QUERY_OPTIONS = "refund_detail_item_list";

    @Override
    public Boolean refund(AliPayH5RefundRequest request) {
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setRefundAmount(request.getRefundAmount());
        model.setOutTradeNo(request.getOrderNo());
        model.setRefundReason(request.getRefundReason());
        model.setOutRequestNo(request.getOutRequestNo());
        // 退款渠道
        List<String> queryOptions = new ArrayList<>();
        queryOptions.add(DEF_QUERY_OPTIONS);
        model.setQueryOptions(queryOptions);
        // 商品信息
        List<RefundGoodsDetail> refundGoodsDetailList = new ArrayList<>();
        List<AliPayH5RefundGoodRequest> goodList = request.getGoodList();
        if (CollectionUtil.isNotEmpty(goodList)) {
            for (AliPayH5RefundGoodRequest goodRequest : goodList) {
                RefundGoodsDetail refundGoodsDetail = new RefundGoodsDetail();
                refundGoodsDetail.setGoodsId(goodRequest.getGoodsId());
                refundGoodsDetail.setRefundAmount(goodRequest.getRefundAmount());
                refundGoodsDetailList.add(refundGoodsDetail);
            }
        }
        model.setRefundGoodsDetail(refundGoodsDetailList);
        AlipayTradeRefundRequest refundRequest = new AlipayTradeRefundRequest();
        refundRequest.setBizModel(model);
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(refundRequest);
        } catch (Exception e) {
            log.error("[AliPay]-refund err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        log.info("[AliPay]-refund result:{}", JSONUtil.toJsonStr(response));
        return response.isSuccess();
    }

}
