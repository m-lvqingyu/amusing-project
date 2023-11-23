package com.amusing.start.platform.enums.code.ali;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
public enum PcPayErrorCode implements BaseCode<PcPayErrorCode> {

    SYSTEM_ERROR("PPP00001", "ACQ.SYSTEM_ERROR", "接口返回错误"),
    INVALID_PARAMETER("PPP00002", "ACQ.INVALID_PARAMETER", "参数无效"),
    ACCESS_FORBIDDEN("PPP00003", "ACQ.ACCESS_FORBIDDEN", "无权限使用接口"),
    EXIST_FORBIDDEN_WORD("PPP00004", "ACQ.EXIST_FORBIDDEN_WORD", "订单信息中包含违禁词"),
    PARTNER_ERROR("PPP00005", "ACQ.PARTNER_ERROR", "应用APP_ID填写错误"),
    TOTAL_FEE_EXCEED("PPP00006", "ACQ.TOTAL_FEE_EXCEED", "订单总金额不在允许范围内"),
    CONTEXT_INCONSISTENT("PPP00007", "ACQ.CONTEXT_INCONSISTENT", "交易信息被篡改"),
    TRADE_HAS_SUCCESS("PPP00008", "ACQ.TRADE_HAS_SUCCESS", "交易已被支付"),
    TRADE_HAS_CLOSE("PPP00009", "ACQ.TRADE_HAS_CLOSE", "交易已经关闭"),
    BUYER_BALANCE_NOT_ENOUGH("PPP00010", "ACQ.BUYER_BALANCE_NOT_ENOUGH", "买家余额不足"),
    BUYER_BANKCARD_BALANCE_NOT_E("PPP00011", "ACQ.BUYER_BANKCARD_BALANCE_NOT_E", "用户银行卡余额不足"),
    ERROR_BALANCE_PAYMENT_DISABL("PPP00012", "ACQ.ERROR_BALANCE_PAYMENT_DISABL", "余额支付功能关闭"),
    BUYER_SELLER_EQUAL("PPP00013", "ACQ.BUYER_SELLER_EQUAL", "买卖家不能相同"),
    TRADE_BUYER_NOT_MATCH("PPP00014", "ACQ.TRADE_BUYER_NOT_MATCH", "交易买家不匹配"),
    BUYER_ENABLE_STATUS_FORBID("PPP00015", "ACQ.BUYER_ENABLE_STATUS_FORBID", "买家状态非法"),
    PAYMENT_FAIL("PPP00016", "ACQ.PAYMENT_FAIL", "支付失败"),
    BUYER_PAYMENT_AMOUNT_DAY_LIM("PPP00017", "ACQ.BUYER_PAYMENT_AMOUNT_DAY_LIM", "买家付款日限额超限"),
    BUYER_PAYMENT_AMOUNT_MONTH_L("PPP00018", "ACQ.BUYER_PAYMENT_AMOUNT_MONTH_L", "买家付款月额度超限"),
    ERROR_BUYER_CERTIFY_LEVEL_LI("PPP00019", "ACQ.ERROR_BUYER_CERTIFY_LEVEL_LI", "买家未通过人行认证"),
    PAYMENT_REQUEST_HAS_RISK("PPP00020", "ACQ.PAYMENT_REQUEST_HAS_RISK", "支付有风险"),
    NO_PAYMENT_INSTRUMENTS_AVAIL("PPP00021", "ACQ.NO_PAYMENT_INSTRUMENTS_AVAIL", "没用可用的支付工具"),
    ILLEGAL_SIGN_VALIDTY_PERIOD("PPP00022", "ACQ.ILLEGAL_SIGN_VALIDTY_PERIOD", "无效的签约有效期"),
    MERCHANT_AGREEMENT_NOT_EXIST("PPP00023", "ACQ.MERCHANT_AGREEMENT_NOT_EXIST", "商户协议不存在"),
    RISK_MERCHANT_IP_NOT_EXIST("PPP00024", "ACQ.RISK_MERCHANT_IP_NOT_EXIST", "当前交易未传入IP信息，创单失败，请传入IP后再发起支付"),
    ;


    private String key;

    private String code;

    private String value;

    PcPayErrorCode(String key, String code, String value) {
        this.key = key;
        this.value = value;
        this.code = code;
    }

    @Override
    public PcPayErrorCode get() {
        return this;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public static PcPayErrorCode getByCode(String code) {
        PcPayErrorCode[] values = PcPayErrorCode.values();
        for (PcPayErrorCode value : values) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

}
