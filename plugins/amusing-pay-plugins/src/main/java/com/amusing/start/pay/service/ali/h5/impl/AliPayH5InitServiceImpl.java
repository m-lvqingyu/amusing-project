package com.amusing.start.pay.service.ali.h5.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.pay.request.AliPayH5InitGoodRequest;
import com.amusing.start.pay.request.AliPayH5InitRequest;
import com.amusing.start.pay.response.AliPayH5InitResponse;
import com.amusing.start.pay.service.ali.h5.AliPayH5InitService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Slf4j
public class AliPayH5InitServiceImpl implements AliPayH5InitService {

    public final AlipayClient alipayClient;

    public AliPayH5InitServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    @Override
    public AliPayH5InitResponse init(AliPayH5InitRequest request) {
        AlipayTradeWapPayRequest payRequest = new AlipayTradeWapPayRequest();
        //异步接收地址，仅支持http/https，公网可访问
        payRequest.setNotifyUrl("");
        //同步跳转地址，仅支持http/https
        payRequest.setReturnUrl("");
        payRequest.setBizContent(buildBizContent(request));
        AlipayTradeWapPayResponse response;
        try {
            response = alipayClient.pageExecute(payRequest, Method.POST.name());
        } catch (Exception e) {
            log.error("[AliPay]-init err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        return new AliPayH5InitResponse()
                .setOrderNo(request.getOrderNo())
                .setAliOrderNo(response.getTradeNo())
                .setBody(response.getBody());
    }

    private static final String OUT_TRADE_NO = "out_trade_no";

    private static final String TOTAL_AMOUNT = "total_amount";

    private static final String SUBJECT = "subject";

    private static final String PRODUCT_CODE = "product_code";

    private static final String TIME_EXPIRE = "time_expire";

    private static final String QUICK_WAP_WAY = "QUICK_WAP_WAY";

    private static final String GOODS_ID = "goods_id";

    private static final String GOODS_NAME = "goods_name";

    private static final String QUANTITY = "quantity";

    private static final String PRICE = "price";

    private static final String GOODS_DETAIL = "goods_detail";

    private String buildBizContent(AliPayH5InitRequest request) {
        JSONObject bizContent = new JSONObject();
        bizContent.set(OUT_TRADE_NO, request.getOrderNo());
        bizContent.set(TOTAL_AMOUNT, request.getAmount());
        bizContent.set(SUBJECT, request.getSubject());
        //手机网站支付默认传值QUICK_WAP_WAY
        bizContent.set(PRODUCT_CODE, QUICK_WAP_WAY);
        bizContent.set(TIME_EXPIRE, request.getTimeExpire());
        List<AliPayH5InitGoodRequest> goodList = request.getGoodList();
        if (CollectionUtil.isNotEmpty(goodList)) {
            JSONArray goodsDetail = new JSONArray();
            for (AliPayH5InitGoodRequest goodRequest : goodList) {
                JSONObject goods = new JSONObject();
                goods.set(GOODS_ID, goodRequest.getGoodsId());
                goods.set(GOODS_NAME, goodRequest.getGoodsName());
                goods.set(QUANTITY, goodRequest.getQuantity());
                goods.set(PRICE, goodRequest.getPrice());
                goodsDetail.add(goods);
            }
            bizContent.set(GOODS_DETAIL, goodsDetail);
        }
        return bizContent.toString();
    }

}
