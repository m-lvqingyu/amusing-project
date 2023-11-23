package com.amusing.start.platform.service.pay.ali.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.output.AliPayTradeOutput;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.AliPayConstant;
import com.amusing.start.platform.enums.code.ali.PayCloseErrorCode;
import com.amusing.start.platform.enums.code.ali.PayRefundErrorCode;
import com.amusing.start.platform.enums.code.ali.PcPayErrorCode;
import com.amusing.start.platform.service.pay.ali.PcPayService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/10/17
 */
@Slf4j
@Service
public class PcPayServiceImpl implements PcPayService {

    @Value("${ali.pay.pc.notify.url}")
    private String aliPayPcNotifyUrl;

    @Value("${ali.pay.pc.return.url}")
    private String aliPayPcReturnUrl;

    public final AlipayClient alipayClient;

    private final OrderClient orderClient;

    @Autowired
    public PcPayServiceImpl(AlipayClient alipayClient,
                            OrderClient orderClient) {
        this.alipayClient = alipayClient;
        this.orderClient = orderClient;
    }

    @Override
    public Boolean close(String orderNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put(AliPayConstant.OUT_TRADE_NO, orderNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeCloseResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-close err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[aliPay]-close result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            return Boolean.TRUE;
        }
        PayCloseErrorCode errorCode = PayCloseErrorCode.getByCode(response.getSubCode());
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    public Boolean refund(String tradeNo, Integer amount, String refundNo) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put(AliPayConstant.TRADE_NO, tradeNo);
        String payAmount = new BigDecimal(amount).divide(
                new BigDecimal(CommConstant.ONE_HUNDRED),
                CommConstant.TWO,
                RoundingMode.HALF_UP
        ).toPlainString();
        bizContent.put(AliPayConstant.REFUND_AMOUNT, payAmount);
        bizContent.put(AliPayConstant.OUT_REQUEST_NO, refundNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeRefundResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-refund err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[aliPay]-refund result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            return Boolean.TRUE;
        }
        PayRefundErrorCode errorCode = PayRefundErrorCode.getByCode(response.getSubCode());
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    public Boolean refundQuery(String tradeNo, String refundNo) {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put(AliPayConstant.TRADE_NO, tradeNo);
        bizContent.put(AliPayConstant.OUT_REQUEST_NO, refundNo);
        request.setBizContent(bizContent.toString());
        AlipayTradeFastpayRefundQueryResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-refundQuery err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[aliPay]-refundQuery result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            return AliPayConstant.REFUND_SUCCESS.equals(response.getRefundStatus());
        }
        PayRefundErrorCode errorCode = PayRefundErrorCode.getByCode(response.getSubCode());
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);

    }

    @Override
    public AliPayTradeOutput query(String orderNo, String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject object = new JSONObject();
        object.put(AliPayConstant.TRADE_NO, tradeNo);
        object.put(AliPayConstant.OUT_TRADE_NO, orderNo);
        request.setBizContent(object.toJSONString());
        AlipayTradeQueryResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-query err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[aliPay]-query result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            AliPayTradeOutput aliPayTradeOutput = new AliPayTradeOutput().setTradeNo(tradeNo)
                    .setOrderNo(orderNo)
                    .setBuyerLogonId(response.getBuyerLogonId())
                    .setTradeStatus(response.getTradeStatus())
                    .setSendPayDate(response.getSendPayDate())
                    .setStoreName(response.getStoreName())
                    .setStoreId(response.getStoreId())
                    .setTerminalId(response.getTerminalId())
                    .setBuyerUserType(response.getBuyerUserType());
            aliPayTradeOutput.setTotalAmount(amountUnitConversion(response.getTotalAmount()));
            aliPayTradeOutput.setBuyerPayAmount(amountUnitConversion(response.getBuyerPayAmount()));
            aliPayTradeOutput.setPointAmount(amountUnitConversion(response.getPointAmount()));
            aliPayTradeOutput.setInvoiceAmount(amountUnitConversion(response.getInvoiceAmount()));
            aliPayTradeOutput.setReceiptAmount(amountUnitConversion(response.getReceiptAmount()));
            aliPayTradeOutput.setMidisCountAmount(amountUnitConversion(response.getMdiscountAmount()));
            aliPayTradeOutput.setDiscountAmount(amountUnitConversion(response.getDiscountAmount()));
            return aliPayTradeOutput;
        }
        PayRefundErrorCode errorCode = PayRefundErrorCode.getByCode(response.getSubCode());
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    /**
     * 元转化为分
     *
     * @param amount 金额。单位：元
     * @return 金额。单位：分
     */
    private Integer amountUnitConversion(String amount) {
        return StringUtil.isBlank(amount) ? CommConstant.ZERO :
                new BigDecimal(amount).multiply(new BigDecimal(CommConstant.ONE_HUNDRED)).intValue();
    }

}
