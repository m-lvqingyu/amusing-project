package com.amusing.start.pay.service.ali.h5.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.pay.service.ali.h5.AliPayH5CloseService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Slf4j
public class AliPayH5CloseServiceImpl implements AliPayH5CloseService {

    private final AlipayClient alipayClient;

    public AliPayH5CloseServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }

    private static final String OUT_TRADE_NO = "out_trade_no";

    @Override
    public Boolean close(String orderNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set(OUT_TRADE_NO, orderNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeCloseResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (Exception e) {
            log.error("[AliPay]-close err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
        log.info("[AliPay]-close result:{}", JSONUtil.toJsonStr(response));
        return response.isSuccess();
    }

}
