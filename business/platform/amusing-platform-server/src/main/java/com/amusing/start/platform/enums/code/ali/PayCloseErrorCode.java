package com.amusing.start.platform.enums.code.ali;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/9
 */
public enum PayCloseErrorCode implements BaseCode<PayCloseErrorCode> {

    SYSTEM_ERROR("PCC00001", "ACQ.SYSTEM_ERROR", "系统异常"),
    TRADE_NOT_EXIST("PCC00002", "ACQ.TRADE_NOT_EXIST", "交易不存在"),
    TRADE_STATUS_ERROR("PCC00003", "ACQ.TRADE_STATUS_ERROR", "交易状态不合法"),
    INVALID_PARAMETER("PCC00004", "ACQ.INVALID_PARAMETER", "参数无效"),
    REASON_TRADE_STATUS_INVALID("PCC00005", "ACQ.REASON_TRADE_STATUS_INVALID", "交易状态异常"),
    REASON_ILLEGAL_STATUS("PCC00006", "ACQ.REASON_ILLEGAL_STATUS", "交易状态异常"),
    SELLER_BALANCE_NOT_ENOUGH("PCC00007", "ACQ.SELLER_BALANCE_NOT_ENOUGH", "商户的支付宝账户中无足够的资金进行撤销"),
    REASON_TRADE_BEEN_FREEZEN("PCC00008", "ACQ.REASON_TRADE_BEEN_FREEZEN", "当前交易被冻结，不允许进行撤销"),
    AQC_SYSTEM_ERROR("PCC00009", "AQC.SYSTEM_ERROR", "系统异常"),
    TRADE_HAS_FINISHED("PCC00010", "ACQ.TRADE_HAS_FINISHED", "交易已经完结"),
    TRADE_CANCEL_TIME_OUT("PCC00011", "ACQ.TRADE_CANCEL_TIME_OUT", "超过撤销时间范围"),
    REASON_TRADE_REFUND_FEE_ERR("PCC00012", "ACQ.REASON_TRADE_REFUND_FEE_ERR", "退款金额无效"),
    CANCEL_NOT_ALLOWED("PCC00013", "ACQ.CANCEL_NOT_ALLOWED", "交易不允许撤销"),
    ;

    private String key;

    private String code;

    private String value;

    PayCloseErrorCode(String key, String code, String value) {
        this.key = key;
        this.value = value;
        this.code = code;
    }

    @Override
    public PayCloseErrorCode get() {
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

    public static PayCloseErrorCode getByCode(String code) {
        PayCloseErrorCode[] values = PayCloseErrorCode.values();
        for (PayCloseErrorCode value : values) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
