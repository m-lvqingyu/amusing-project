package com.amusing.start.platform.service.pay.ali.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.amusing.start.client.api.OrderClient;
import com.amusing.start.client.output.OrderDetailOutput;
import com.amusing.start.client.output.pay.ali.BarCodePayOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.platform.constant.AliPayConstant;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayCreateVo;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayPreCreateVo;
import com.amusing.start.platform.enums.code.ali.BarCodePayErrorCode;
import com.amusing.start.platform.enums.code.ali.PcPayErrorCode;
import com.amusing.start.platform.enums.code.ali.ScanCodePayErrorCode;
import com.amusing.start.platform.service.pay.ali.AliTradeCreateService;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/10
 */
@Slf4j
@Service
public class AliTradeCreateServiceImpl implements AliTradeCreateService {

    @Value("${ali.pay.pc.notify.url}")
    private String aliPayPcNotifyUrl;

    @Value("${ali.pay.pre.notify.url}")
    private String aliPayPreNotifyUrl;

    @Value("${ali.pay.pc.return.url}")
    private String aliPayPcReturnUrl;

    private final AlipayClient alipayClient;

    private final OrderClient orderClient;

    @Autowired
    public AliTradeCreateServiceImpl(AlipayClient alipayClient,
                                     OrderClient orderClient) {
        this.alipayClient = alipayClient;
        this.orderClient = orderClient;
    }

    private static final String TEN_MINUTES = "10m";

    @Override
    public String payFormPc(String orderNo) {
        OrderDetailOutput output = getOrderDetail(orderNo);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayPcNotifyUrl);
        request.setReturnUrl(aliPayPcReturnUrl);
        JSONObject bizContent = AliPayConstant.buildCommPayRq(orderNo, output.getRealAmount(), output.getDescribe());
        // 电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put(AliPayConstant.PRODUCT_CODE, AliPayConstant.FAST_INSTANT_TRADE_PAY);
        // 过期时间
        DateTime dateTime = DateUtil.offsetMinute(new Date(), CommConstant.FIFTEEN);
        bizContent.put(AliPayConstant.TIME_EXPIRE, DateUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN));
        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response;
        try {
            response = alipayClient.pageExecute(request);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-pc pay err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[aliPay]-pc pay result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            return response.getBody();
        }
        String code = response.getCode();
        PcPayErrorCode errorCode = PcPayErrorCode.getByCode(code);
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    @Override
    public BarCodePayOutput payFormBarCode(String orderNo, String authCode) {
        OrderDetailOutput output = getOrderDetail(orderNo);
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setNotifyUrl(aliPayPcNotifyUrl);
        JSONObject bizContent = AliPayConstant.buildCommPayRq(orderNo, output.getRealAmount(), output.getDescribe());
        // 条码场景：bar_code
        bizContent.put(AliPayConstant.SCENE, AliPayConstant.BAR_CODE);
        // 根据auth_code_type上传付款码，auth_code_type=bar_code，则填写用户支付宝钱包中的付款码；
        // auth_code_type=security_code，则填写设备刷脸返回的付款码；
        bizContent.put(AliPayConstant.AUTH_CODE, authCode);
        request.setBizContent(bizContent.toString());
        AlipayTradePayResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[BarCodePay]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        log.info("[BarCodePay]-result:{}", JSONObject.toJSONString(response));
        if (response.isSuccess()) {
            return new BarCodePayOutput().setTradeNo(response.getTradeNo())
                    .setOrderNo(orderNo)
                    .setBuyerLogonId(response.getBuyerLogonId())
                    .setGmtPayment(response.getGmtPayment())
                    .setTotalAmount(getAmount(response.getTotalAmount()))
                    .setReceiptAmount(getAmount(response.getReceiptAmount()))
                    .setBuyerPayAmount(getAmount(response.getBuyerPayAmount()))
                    .setPointAmount(getAmount(response.getPointAmount()))
                    .setInvoiceAmount(getAmount(response.getInvoiceAmount()))
                    .setMerchantDiscountAmount(getAmount(response.getMdiscountAmount()))
                    .setDiscountAmount(getAmount(response.getDiscountAmount()));
        }
        String subCode = response.getSubCode();
        BarCodePayErrorCode errorCode = BarCodePayErrorCode.getByCode(subCode);
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    @Override
    public ScanCodePayPreCreateVo payFormPreScanCode(String orderNo) {
        OrderDetailOutput output = getOrderDetail(orderNo);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(aliPayPreNotifyUrl);
        JSONObject bizContent = AliPayConstant.buildCommPayRq(orderNo, output.getRealAmount(), output.getDescribe());
        request.setBizContent(bizContent.toString());
        AlipayTradePrecreateResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[PreScanCodePay]-preCreate err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        if (response.isSuccess()) {
            return new ScanCodePayPreCreateVo()
                    .setOrderNo(orderNo)
                    .setQrCode(response.getQrCode())
                    .setShareCode(response.getShareCode());
        }
        String code = response.getCode();
        ScanCodePayErrorCode errorCode = ScanCodePayErrorCode.getByCode(code);
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }
    
    public ScanCodePayCreateVo payFormScanCode(String orderNo, String buyerId) {
        OrderDetailOutput output = getOrderDetail(orderNo);
        JSONObject bizContent = AliPayConstant.buildCommPayRq(orderNo, output.getRealAmount(), output.getDescribe());
        bizContent.put(AliPayConstant.BUYER_ID, buyerId);
        // 订单相对超时时间。从交易创建时间开始计算。
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天
        bizContent.put(AliPayConstant.TIMEOUT_EXPRESS, TEN_MINUTES);
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setNotifyUrl(aliPayPcNotifyUrl);
        request.setBizContent(bizContent.toString());
        AlipayTradeCreateResponse response;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            log.error("[ScanCodePay]-preCreate err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        if (response.isSuccess()) {
            return new ScanCodePayCreateVo().setOrderNo(response.getOutTradeNo()).setTradeNo(response.getTradeNo());
        }
        String code = response.getCode();
        ScanCodePayErrorCode errorCode = ScanCodePayErrorCode.getByCode(code);
        if (errorCode == null) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        throw new CustomException(errorCode);
    }

    private OrderDetailOutput getOrderDetail(String orderNo) {
        ApiResult<OrderDetailOutput> apiResult = orderClient.orderDetail(orderNo);
        if (apiResult == null || !apiResult.isSuccess()) {
            throw new CustomException(CommCode.SERVICE_ERR);
        }
        return apiResult.getData();
    }

    private Integer getAmount(String amount) {
        if (StringUtils.isBlank(amount)) {
            return CommConstant.ZERO;
        }
        return new BigDecimal(amount).multiply(new BigDecimal(CommConstant.ONE_HUNDRED)).intValue();
    }


}
