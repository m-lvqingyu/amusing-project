package com.amusing.start.platform.enums.code.ali;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
public enum PayRefundErrorCode implements BaseCode<PayRefundErrorCode> {

    SYSTEM_ERROR("PRF00001", "ACQ.SYSTEM_ERROR", "系统错误"),
    INVALID_PARAMETER("PRF00002", "ACQ.INVALID_PARAMETER", "参数无效"),
    SELLER_BALANCE_NOT_ENOUGH("PRF00003", "ACQ.SELLER_BALANCE_NOT_ENOUGH", "卖家余额不足"),
    REFUND_AMT_NOT_EQUAL_TOTAL("PRF00004", "ACQ.REFUND_AMT_NOT_EQUAL_TOTAL", "退款金额超限"),
    REASON_TRADE_BEEN_FREEZEN("PRF00005", "ACQ.REASON_TRADE_BEEN_FREEZEN", "请求退款的交易被冻结"),
    TRADE_NOT_EXIST("PRF00006", "ACQ.TRADE_NOT_EXIST", "交易不存在"),
    TRADE_HAS_FINISHED("PRF00007", "ACQ.TRADE_HAS_FINISHED", "交易已完结"),
    TRADE_STATUS_ERROR("PRF00008", "ACQ.TRADE_STATUS_ERROR", "交易状态非法"),
    DISCORDANT_REPEAT_REQUEST("PRF00009", "ACQ.DISCORDANT_REPEAT_REQUEST", "请求信息不一致"),
    REASON_TRADE_REFUND_FEE_ERR("PRF00010", "ACQ.REASON_TRADE_REFUND_FEE_ERR", "退款金额无效"),
    TRADE_NOT_ALLOW_REFUND("PRF00011", "ACQ.TRADE_NOT_ALLOW_REFUND", "当前交易不允许退款"),
    REFUND_FEE_ERROR("PRF00012", "ACQ.REFUND_FEE_ERROR", "交易退款金额有误"),
    TRADE_HAS_CLOSE("PRF00013", "ACQ.TRADE_HAS_CLOSE", "交易已关闭"),
    BUYER_NOT_EXIST("PRF00014", "ACQ.BUYER_NOT_EXIST", "买家不存在"),
    BUYER_ENABLE_STATUS_FORBID("PRF00015", "ACQ.BUYER_ENABLE_STATUS_FORBID", "买家状态异常"),
    REASON_TRADE_STATUS_INVALID("PRF00016", "ACQ.REASON_TRADE_STATUS_INVALID", "交易状态异常"),
    NOT_ALLOW_PARTIAL_REFUND("PRF00017", "ACQ.NOT_ALLOW_PARTIAL_REFUND", "不支持部分退款"),
    ONLINE_TRADE_VOUCHER_NOT_ALLOW_REFUND("PRF00018", "ACQ.ONLINE_TRADE_VOUCHER_NOT_ALLOW_REFUND", "交易不允许退款"),
    BUYER_ERROR("PRF00019", "ACQ.BUYER_ERROR", "买家状态异常"),
    CURRENCY_NOT_SUPPORT("PRF00020", "ACQ.CURRENCY_NOT_SUPPORT", "退款币种不支持"),
    ALLOC_AMOUNT_VALIDATE_ERROR("PRF00021", "ACQ.ALLOC_AMOUNT_VALIDATE_ERROR", "退分账金额超限"),
    USER_NOT_MATCH_ERR("PRF00022", "ACQ.USER_NOT_MATCH_ERR", "交易用户不匹配"),
    TRADE_SETTLE_ERROR("PRF00023", "ACQ.TRADE_SETTLE_ERROR", "交易结算异常"),
    REFUND_CHARGE_ERROR("PRF00024", "ACQ.REFUND_CHARGE_ERROR", "退收费异常"),
    ENTERPRISE_PAY_BIZ_ERROR("PRF00025", "ACQ.ENTERPRISE_PAY_BIZ_ERROR", "因公付业务异常"),
    OVERDRAFT_ASSIGN_ACCOUNT_INVALID("PRF00026", "ACQ.OVERDRAFT_ASSIGN_ACCOUNT_INVALID", "垫资退款出资账号和商户信息不一致"),
    OVERDRAFT_AGREEMENT_NOT_MATCH("PRF00027", "ACQ.OVERDRAFT_AGREEMENT_NOT_MATCH", "垫资退款接口传入模式和签约配置不一致"),
    REFUND_ACCOUNT_NOT_EXIST("PRF00028", "ACQ.REFUND_ACCOUNT_NOT_EXIST", "退款出资账号不存在或账号异常"),
    CUSTOMER_VALIDATE_ERROR("PRF00029", "ACQ.CUSTOMER_VALIDATE_ERROR", "账户已注销或者被冻结"),
    REFUND_ROYALTY_PAYEE_ACCOUNT_NOT_EXIST("PRF00030", "ACQ.REFUND_ROYALTY_PAYEE_ACCOUNT_NOT_EXIST", "退分账收入方账户不存在"),
    TRADE_NOT_EXIST_QU("PRF00031", "TRADE_NOT_EXIST", "查询退款的交易不存在"),
    ;


    private String key;

    private String code;

    private String value;

    PayRefundErrorCode(String key, String code, String value) {
        this.key = key;
        this.value = value;
        this.code = code;
    }

    @Override
    public PayRefundErrorCode get() {
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

    public static PayRefundErrorCode getByCode(String code) {
        PayRefundErrorCode[] values = PayRefundErrorCode.values();
        for (PayRefundErrorCode value : values) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
