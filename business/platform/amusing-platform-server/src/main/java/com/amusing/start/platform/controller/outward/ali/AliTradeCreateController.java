package com.amusing.start.platform.controller.outward.ali;

import com.amusing.start.client.output.pay.ali.BarCodePayOutput;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayCreateVo;
import com.amusing.start.platform.entity.vo.pay.ali.ScanCodePayPreCreateVo;
import com.amusing.start.platform.service.pay.ali.AliTradeCreateService;
import com.amusing.start.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Lv.QingYu
 * @since 2023/11/8
 */
@Api(value = "支付宝-统一下单入口")
@Slf4j
@RestController
@RequestMapping(("platform/outward/ali"))
public class AliTradeCreateController {

    private final AliTradeCreateService aliTradeCreateService;

    @Autowired
    public AliTradeCreateController(AliTradeCreateService aliTradeCreateService) {
        this.aliTradeCreateService = aliTradeCreateService;
    }

    @ApiOperation("支付宝-电脑网站支付")
    @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true)
    @GetMapping("pc/pay")
    public ApiResult<String> pcPay(@NotBlank(message = "订单号不能为空")
                                   @Pattern(regexp = "^[A-Z][A-Z0-9]{15,31}$", message = "订单号不合法")
                                   @RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(aliTradeCreateService.payFormPc(orderNo));
    }

    @ApiOperation("支付宝-付款码支付-商家扫用户付款码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true),
            @ApiImplicitParam(name = "authCode", value = "用户付款码", required = true)
    })
    @GetMapping("bar/code/pay")
    public ApiResult<BarCodePayOutput> barCodePay(@NotBlank(message = "订单号不能为空")
                                                  @Pattern(regexp = "^[A-Z][A-Z0-9]{15,31}$", message = "订单号不合法")
                                                  @RequestParam("orderNo") String orderNo,
                                                  @NotBlank(message = "用户付款码不能为空")
                                                  @Pattern(regexp = "[A-Z0-9]{5,31}$", message = "付款码不合法")
                                                  @RequestParam("authCode") String authCode) {
        return ApiResult.ok(aliTradeCreateService.payFormBarCode(orderNo, authCode));
    }

    @ApiOperation("支付宝-扫码支付-一码一扫")
    @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true)
    @GetMapping("pre/scan/code/pay")
    public ApiResult<ScanCodePayPreCreateVo> preScanCodePay(@NotBlank(message = "订单编号不能为空")
                                                            @Pattern(regexp = "^[A-Z][A-Z0-9]{15,31}$", message = "订单号不合法")
                                                            @RequestParam("orderNo") String orderNo) {
        return ApiResult.ok(aliTradeCreateService.payFormPreScanCode(orderNo));
    }

    @ApiOperation("支付宝-扫码支付-一码多扫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单编号", required = true),
            @ApiImplicitParam(name = "buyerId", value = "买家支付宝用户Id", required = true)
    })
    @GetMapping("scan/code/pay")
    public ApiResult<ScanCodePayCreateVo> scanCodePay(@NotBlank(message = "订单编号不能为空")
                                                      @Pattern(regexp = "^[A-Z][A-Z0-9]{15,31}$", message = "订单号不合法")
                                                      @RequestParam("orderNo") String orderNo,
                                                      @NotBlank(message = "买家支付宝用户Id")
                                                      @RequestParam("buyerId") String buyerId) {
        return ApiResult.ok(aliTradeCreateService.payFormScanCode(orderNo, buyerId));
    }

}
