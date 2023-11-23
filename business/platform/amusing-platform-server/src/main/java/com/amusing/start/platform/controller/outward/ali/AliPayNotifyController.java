package com.amusing.start.platform.controller.outward.ali;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.amusing.start.platform.constant.AliPayConstant;
import com.amusing.start.platform.enums.AliTradeOrderStatus;
import com.amusing.start.platform.service.pay.ali.PayNotifyService;
import com.google.common.base.Throwables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lv.QingYu
 * @since 2023/10/30
 */
@Api(value = "支付宝-回调通知")
@Slf4j
@RestController
@RequestMapping(("platform/out/ali"))
public class AliPayNotifyController {

    @Value("${ali.public.key}")
    private String aliPublicKey;

    private final PayNotifyService aliPcPayNotifyService;

    @Autowired
    public AliPayNotifyController(PayNotifyService aliPcPayNotifyService) {
        this.aliPcPayNotifyService = aliPcPayNotifyService;
    }

    @ApiOperation("电脑网站支付-同步回调：支付成功后会跳转到该地址的页面，且返回同步通知参数")
    @RequestMapping("pc/sync/notify")
    public String syncNotify(HttpServletRequest request) {
        Map<String, String> params = getRequestParam(request);
        if (CollectionUtil.isEmpty(params)) {
            return AliPayConstant.FAIL;
        }
        log.info("[PcSyncNotify]-param:{}", JSONObject.toJSONString(params));
        return paramSignCheck(params) ? AliPayConstant.SUCCESS : AliPayConstant.FAIL;
    }

    @ApiOperation("电脑网站支付-异步回调(支付成功-支付成功触发)")
    @RequestMapping("pc/async/notify")
    public String pcAsyncNotify(HttpServletRequest request) {
        Map<String, String> params = getRequestParam(request);
        if (CollectionUtil.isEmpty(params)) {
            return AliPayConstant.FAIL;
        }
        log.info("[PcAsyncNotify]-param:{}", JSONObject.toJSONString(params));
        String orderNo = params.get(AliPayConstant.OUT_TRADE_NO);
        if (StringUtils.isBlank(orderNo)) {
            return AliPayConstant.FAIL;
        }
        String tradeNo = params.get(AliPayConstant.TRADE_NO);
        if (StringUtils.isBlank(tradeNo)) {
            return AliPayConstant.FAIL;
        }
        String tradeStatus = params.get(AliPayConstant.TRADE_STATUS);
        if (AliTradeOrderStatus.getByName(tradeStatus) == null) {
            return AliPayConstant.FAIL;
        }
        String totalAmount = params.get(AliPayConstant.TOTAL_AMOUNT);
        if (StringUtils.isBlank(totalAmount)) {
            return AliPayConstant.FAIL;
        }
        String receiptAmount = params.get(AliPayConstant.RECEIPT_AMOUNT);
        if (StringUtils.isBlank(receiptAmount)) {
            return AliPayConstant.FAIL;
        }
        String notifyId = params.get(AliPayConstant.NOTIFY_ID);
        if (StringUtils.isBlank(notifyId)) {
            return AliPayConstant.FAIL;
        }
        boolean signature = paramSignCheck(params);
        if (!signature) {
            return AliPayConstant.FAIL;
        }
        return aliPcPayNotifyService.pcAsyncNotify(notifyId,
                params.get(AliPayConstant.BUYER_ID),
                orderNo,
                tradeNo,
                tradeStatus,
                totalAmount,
                receiptAmount,
                params.get(AliPayConstant.INVOICE_AMOUNT),
                params.get(AliPayConstant.BUYER_PAY_AMOUNT),
                params.get(AliPayConstant.POINT_AMOUNT),
                params.get(AliPayConstant.GMT_PAYMENT)
        );
    }

    private Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            params.put(name, request.getParameter(name));
        }
        return params;
    }

    /**
     * @param params 请求参数
     * @return true:通过 false:失败
     * @description: 参数验签检查
     */
    private boolean paramSignCheck(Map<String, String> params) {
        String sign = params.get(AliPayConstant.SIGN);
        String content = AlipaySignature.getSignCheckContentV1(params);
        try {
            return AlipaySignature.rsa256CheckContent(
                    content,
                    sign,
                    aliPublicKey,
                    AlipayConstants.CHARSET_UTF8);
        } catch (AlipayApiException e) {
            log.error("[aliPay]-signature err! content:{}, sign:{}, msg:{}",
                    content,
                    sign,
                    Throwables.getStackTraceAsString(e)
            );
            return Boolean.FALSE;
        }
    }


}
