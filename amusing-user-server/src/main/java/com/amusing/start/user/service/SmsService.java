package com.amusing.start.user.service;

/**
 * @author Lv.QingYu
 * @since 2024/12/3
 */
public interface SmsService {

    void sendRegisterCode(String phone, String code);

    String getRegisterCode(String phone);
}
