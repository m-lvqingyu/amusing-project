package com.amusing.start.platform.service.pay.ali.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.output.pay.ali.BarCodePayOutput;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.client.output.pay.ali.CancelOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.AliPayConstant;
import com.amusing.start.platform.enums.code.ali.BarCodePayErrorCode;
import com.amusing.start.platform.enums.code.ali.PayCloseErrorCode;
import com.amusing.start.platform.service.pay.ali.BarCodePayService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
@Slf4j
@Service
public class BarCodePayServiceImpl implements BarCodePayService {

    private final AlipayClient alipayClient;


    @Autowired
    public BarCodePayServiceImpl(AlipayClient alipayClient) {
        this.alipayClient = alipayClient;
    }


    @Override
    public CancelOutput cancel(String orderNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put(AliPayConstant.OUT_TRADE_NO, orderNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeCancelResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[PayCancel]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        if (response.isSuccess()) {
            return new CancelOutput()
                    .setTradeNo(response.getTradeNo())
                    .setOrderNo(orderNo)
                    .setRetryFlag(AliPayConstant.Y.equalsIgnoreCase(response.getRetryFlag()))
                    .setAction(response.getAction());
        }
        String subCode = response.getSubCode();
        PayCloseErrorCode errorCode = PayCloseErrorCode.getByCode(subCode);
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

}
