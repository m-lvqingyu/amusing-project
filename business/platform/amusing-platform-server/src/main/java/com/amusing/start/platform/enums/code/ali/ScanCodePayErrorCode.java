package com.amusing.start.platform.enums.code.ali;

import com.amusing.start.code.BaseCode;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/10
 */
public enum ScanCodePayErrorCode implements BaseCode<ScanCodePayErrorCode> {

    SYSTEM_ERROR("PSC00001", "ACQ.SYSTEM_ERROR", "接口返回错误"),
    INVALID_PARAMETER("PSC00002", "ACQ.INVALID_PARAMETER", "参数无效"),
    ACCESS_FORBIDDEN("PSC00003", "ACQ.ACCESS_FORBIDDEN", "无权限使用接口"),
    EXIST_FORBIDDEN_WORD("PSC00004", "ACQ.EXIST_FORBIDDEN_WORD", "订单信息中包含违禁词"),
    PARTNER_ERROR("PSC00005", "ACQ.PARTNER_ERROR", "应用APP_ID填写错误"),
    TOTAL_FEE_EXCEED("PSC00006", "ACQ.TOTAL_FEE_EXCEED", "订单总金额超过限额"),
    CONTEXT_INCONSISTENT("PSC00007", "ACQ.CONTEXT_INCONSISTENT", "交易信息被篡改"),
    TRADE_HAS_SUCCESS("PSC00008", "ACQ.TRADE_HAS_SUCCESS", "交易已被支付"),
    TRADE_HAS_CLOSE("PSC00009", "ACQ.TRADE_HAS_CLOSE", "交易已经关闭"),
    BUYER_SELLER_EQUAL("PSC00010", "ACQ.BUYER_SELLER_EQUAL", "买卖家不能相同"),
    TRADE_BUYER_NOT_MATCH("PSC00011", "ACQ.TRADE_BUYER_NOT_MATCH", "交易买家不匹配"),
    BUYER_ENABLE_STATUS_FORBID("PSC00012", "ACQ.BUYER_ENABLE_STATUS_FORBID", "买家状态非法"),
    BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR("PSC00013", "ACQ.BUYER_PAYMENT_AMOUNT_DAY_LIMIT_ERROR", "买家付款日限额超限"),
    BEYOND_PAY_RESTRICTION("PSC00014", "ACQ.BEYOND_PAY_RESTRICTION", "商户收款额度超限"),
    BEYOND_PER_RECEIPT_RESTRICTION("PSC00015", "ACQ.BEYOND_PER_RECEIPT_RESTRICTION", "商户收款金额超过月限额"),
    BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR("PSC00016", "ACQ.BUYER_PAYMENT_AMOUNT_MONTH_LIMIT_ERROR", "买家付款月额度超限"),
    SELLER_BEEN_BLOCKED("PSC00017", "ACQ.SELLER_BEEN_BLOCKED", "商家账号被冻结"),
    ERROR_BUYER_CERTIFY_LEVEL_LIMIT("PSC00018", "ACQ.ERROR_BUYER_CERTIFY_LEVEL_LIMIT", "买家未通过人行认证"),
    INVALID_STORE_ID("PSC00019", "ACQ.INVALID_STORE_ID", "商户门店编号无效"),
    APPLY_PC_MERCHANT_CODE_ERROR("PSC00020", "ACQ.APPLY_PC_MERCHANT_CODE_ERROR", "申请二维码失败"),
    SECONDARY_MERCHANT_STATUS_ERROR("PSC00021", "ACQ.SECONDARY_MERCHANT_STATUS_ERROR", "商户状态异常"),
    BEYOND_PER_RECEIPT_DAY_RESTRICTION("PSC00022", "ACQ.BEYOND_PER_RECEIPT_DAY_RESTRICTION", "订单金额超过当日累计限额"),
    BEYOND_PER_RECEIPT_SINGLE_RESTRICTION("PSC00023", "ACQ.BEYOND_PER_RECEIPT_SINGLE_RESTRICTION", "订单金额超过单笔限额"),
    TRADE_SETTLE_ERROR("PSC00024", "ACQ.TRADE_SETTLE_ERROR", "交易结算异常"),
    SECONDARY_MERCHANT_ID_INVALID("PSC00025", "ACQ.SECONDARY_MERCHANT_ID_INVALID", "二级商户不存在"),
    SECONDARY_MERCHANT_ISV_PUNISH_INDIRECT("PSC00026", "ACQ.SECONDARY_MERCHANT_ISV_PUNISH_INDIRECT", "商户状态异常"),
    SELLER_NOT_EXIST("PSC00027", "ACQ.SELLER_NOT_EXIST", "卖家不存在"),
    SECONDARY_MERCHANT_ALIPAY_ACCOUNT_INVALID("PSC00028", "ACQ.SECONDARY_MERCHANT_ALIPAY_ACCOUNT_INVALID", "二级商户账户异常"),
    INVALID_RECEIVE_ACCOUNT("PSC00029", "ACQ.INVALID_RECEIVE_ACCOUNT", "收款账户不支持"),
    SECONDARY_MERCHANT_ID_BLANK("PSC00030", "ACQ.SECONDARY_MERCHANT_ID_BLANK", "二级商户编号错误"),
    NOW_TIME_AFTER_EXPIRE_TIME_ERROR("PSC00031", "ACQ.NOW_TIME_AFTER_EXPIRE_TIME_ERROR", "当前时间已超过允许支付的时间"),
    SECONDARY_MERCHANT_NOT_MATCH("PSC00032", "ACQ.SECONDARY_MERCHANT_NOT_MATCH", "二级商户信息不匹配"),
    BUYER_NOT_EXIST("PSC00033", "ACQ.BUYER_NOT_EXIST", "买家不存在"),
    SUB_GOODS_SIZE_MAX_COUNT("PSC00034", "ACQ.SUB_GOODS_SIZE_MAX_COUNT", "子商品明细超长"),
    DEFAULT_SETTLE_RULE_NOT_EXIST("PSC00035", "ACQ.DEFAULT_SETTLE_RULE_NOT_EXIST", "默认结算条款不存在"),
    MERCHANT_PERM_RECEIPT_SUSPEND_LIMIT("PSC00036", "ACQ.MERCHANT_PERM_RECEIPT_SUSPEND_LIMIT", "商户暂停收款"),
    MERCHANT_PERM_RECEIPT_SINGLE_LIMIT("PSC00037", "ACQ.MERCHANT_PERM_RECEIPT_SINGLE_LIMIT", "超过单笔收款限额"),
    MERCHANT_PERM_RECEIPT_DAY_LIMIT("PSC00038", "ACQ.MERCHANT_PERM_RECEIPT_DAY_LIMIT", "超过单日累计收款额度"),
    RISK_MERCHANT_IP_NOT_EXIST("PSC00039", "ACQ.RISK_MERCHANT_IP_NOT_EXIST", "当前交易未传入IP信息，创单失败，请传入IP后再发起支付"),
    MERCHANT_STATUS_NOT_NORMAL("PSC00040", "ACQ.MERCHANT_STATUS_NOT_NORMAL", "商户状态异常"),
    BIZ_PRODUCT_NOT_ALLOWED("PSC00041", "ACQ.BIZ_PRODUCT_NOT_ALLOWED", "公域订单场景下不支持使用该产品码"),
    CUSTOMER_VALIDATE_ERROR("PSC00042", "ACQ.CUSTOMER_VALIDATE_ERROR", "客户校验出错"),
    ERROR_SELLER_CERTIFY_LEVEL_LIMIT("PSC00043", "ACQ.ERROR_SELLER_CERTIFY_LEVEL_LIMIT", "卖家未通过人行认证"),
    ILLEGAL_ARGUMENT("PSC00044", "ACQ.ILLEGAL_ARGUMENT", "参数错误"),
    NOT_SUPPORT_PAYMENT_INST("PSC00045", "ACQ.NOT_SUPPORT_PAYMENT_INST", "不支持的钱包版本"),
    OPEN_ID_NOT_TINY_APP("PSC00046", "ACQ.OPEN_ID_NOT_TINY_APP", "请求的应用id非小程序应用类型"),
    PAYER_UNMATCHED("PSC00047", "ACQ.PAYER_UNMATCHED", "付款人不匹配"),
    PLATFORM_BUSINESS_ACQUIRE_MODE_MUST_MERCHANT_ID("PSC00048", "ACQ.PLATFORM_BUSINESS_ACQUIRE_MODE_MUST_MERCHANT_ID", "二级商户编码为空"),
    PRODUCT_NOT_SUPPORT_IN_TINY_APP("PSC00049", "ACQ.PRODUCT_NOT_SUPPORT_IN_TINY_APP", "小程序内不支持使用该产品码交易"),
    SECONDARY_MERCHANT_CARD_ALIAS_NO_INVALID("PSC00050", "ACQ.SECONDARY_MERCHANT_CARD_ALIAS_NO_INVALID", "二级商户银行卡编号错误"),
    STORE_INFO_INVALID("PSC00051", "ACQ.STORE_INFO_INVALID", "门店信息错误"),
    SUB_MERCHANT_CREATE_FAIL("PSC00052", "ACQ.SUB_MERCHANT_CREATE_FAIL", "二级商户创建失败"),
    SUB_MERCHANT_TYPE_INVALID("PSC00053", "ACQ.SUB_MERCHANT_TYPE_INVALID", "二级商户类型非法"),
    UNBOUND_APPLICATION("PSC00054", "ACQ.UNBOUND_APPLICATION", "未绑定小程序appId"),
    USER_LOGON_ID_DUP("PSC00055", "ACQ.USER_LOGONID_DUP", "用户账号重复"),
    ;

    private String key;

    private String code;

    private String value;

    ScanCodePayErrorCode(String key, String code, String value) {
        this.key = key;
        this.value = value;
        this.code = code;
    }

    @Override
    public ScanCodePayErrorCode get() {
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

    public static ScanCodePayErrorCode getByCode(String code) {
        ScanCodePayErrorCode[] values = ScanCodePayErrorCode.values();
        for (ScanCodePayErrorCode value : values) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

}
