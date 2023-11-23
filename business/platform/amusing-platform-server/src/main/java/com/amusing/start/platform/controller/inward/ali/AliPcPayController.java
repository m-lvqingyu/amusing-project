package com.amusing.start.platform.controller.inward.ali;

import com.amusing.start.client.api.AliPcPayClient;
import com.amusing.start.client.output.AliPayTradeOutput;
import com.amusing.start.platform.enums.AliPayAsyncNotifyStatus;
import com.amusing.start.platform.service.pay.ali.PayNotifyService;
import com.amusing.start.platform.service.pay.ali.PcPayService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lv.QingYu
 * @since 2023/10/17
 */
@Slf4j
@RestController
public class AliPcPayController implements AliPcPayClient {

    private final PcPayService aliPcPayService;

    private final PayNotifyService aliPcPayNotifyService;

    @Autowired
    public AliPcPayController(PcPayService aliPcPayService,
                              PayNotifyService aliPcPayNotifyService) {
        this.aliPcPayService = aliPcPayService;
        this.aliPcPayNotifyService = aliPcPayNotifyService;
    }

    @Override
    public ApiResult<Boolean> close(String orderNo) {
        return ApiResult.ok(aliPcPayService.close(orderNo));
    }

    @Override
    public ApiResult<Boolean> refund(String tradeNo, Integer amount, String refundNo) {
        return ApiResult.ok(aliPcPayService.refund(tradeNo, amount, refundNo));
    }

    @Override
    public ApiResult<Boolean> refundQuery(String tradeNo, String refundNo) {
        return ApiResult.ok(aliPcPayService.refundQuery(tradeNo, refundNo));
    }

    @Override
    public ApiResult<AliPayTradeOutput> query(String orderNo, String tradeNo) {
        return ApiResult.ok(aliPcPayService.query(orderNo, tradeNo));
    }

    @Override
    public ApiResult<Boolean> asyncNotifyFinish(Long id) {
        return ApiResult.ok(aliPcPayNotifyService.updateAsyncNotifyStatus(id, AliPayAsyncNotifyStatus.FINISH.getKey()));
    }

    @Override
    public ApiResult<Boolean> asyncNotifyIgnore(Long id) {
        return ApiResult.ok(aliPcPayNotifyService.updateAsyncNotifyStatus(id, AliPayAsyncNotifyStatus.IGNORE.getKey()));
    }

}
