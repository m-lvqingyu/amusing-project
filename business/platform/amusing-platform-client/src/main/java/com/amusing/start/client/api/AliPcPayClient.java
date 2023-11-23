package com.amusing.start.client.api;

import com.amusing.start.client.fallback.AliPcPayClientFallback;
import com.amusing.start.client.output.AliPayTradeOutput;
import com.amusing.start.result.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Lv.QingYu
 * @description: 支付宝电脑网站支付接口
 * @since 2023/10/17
 */
@FeignClient(
        name = "amusing-platform-server",
        fallbackFactory = AliPcPayClientFallback.class,
        contextId = "amusing-platform-server-AliPcPayClient"
)
public interface AliPcPayClient {

    /**
     * @param orderNo 订单编号
     * @return true:成功 false:失败
     * @description: 关闭交易接口(只有等待买家付款状态下才能发起交易关闭.也就是用户已经登录支付宝, 但是还未输入密码支付这段时间内, 可以关闭交易)
     */
    @PostMapping("platform/in/ali/pay/close")
    ApiResult<Boolean> close(@RequestParam("orderNo") String orderNo);

    /**
     * @param tradeNo  支付宝交易号
     * @param amount   退款金额(单位分)
     * @param refundNo 退款编号
     * @return true：成功 false：失败
     * @description: 统一收单交易退款接口
     */
    @PostMapping("platform/in/ali/pay/refund")
    ApiResult<Boolean> refund(@RequestParam("tradeNo") String tradeNo, @RequestParam("amount") Integer amount, @RequestParam("refundNo") String refundNo);

    /**
     * @param tradeNo  支付宝交易号
     * @param refundNo 退款编号
     * @return true：成功 false：失败
     * @description: 统一收单交易退款查询
     */
    @PostMapping("platform/in/ali/pay/refund/query")
    ApiResult<Boolean> refundQuery(@RequestParam("tradeNo") String tradeNo, @RequestParam("refundNo") String refundNo);

    /**
     * @param orderNo 订单编号
     * @param tradeNo 支付宝交易号
     * @return 交易信息
     * @description: 统一收单交易查询
     */
    @GetMapping("platform/in/ali/pay/query")
    ApiResult<AliPayTradeOutput> query(@RequestParam("orderNo") String orderNo, @RequestParam("tradeNo") String tradeNo);


    @PostMapping("platform/in/ali/notify/finish")
    ApiResult<Boolean> asyncNotifyFinish(@RequestParam("id") Long id);

    @PostMapping("platform/in/ali/notify/ignore")
    ApiResult<Boolean> asyncNotifyIgnore(@RequestParam("id") Long id);


}
