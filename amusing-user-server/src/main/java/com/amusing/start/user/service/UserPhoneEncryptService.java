package com.amusing.start.user.service;

import com.amusing.start.user.entity.pojo.UserPhoneEncrypt;

/**
 * @author Lv.QingYu
 * @since 2024/10/25
 */
public interface UserPhoneEncryptService {

    UserPhoneEncrypt get(String userId);

    Integer update(UserPhoneEncrypt encrypt);

    Integer save(UserPhoneEncrypt encrypt);
}
