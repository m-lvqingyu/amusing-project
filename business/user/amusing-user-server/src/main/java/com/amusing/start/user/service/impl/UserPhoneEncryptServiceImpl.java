package com.amusing.start.user.service.impl;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.user.entity.pojo.UserPhoneEncrypt;
import com.amusing.start.user.mapper.UserPhoneEncryptMapper;
import com.amusing.start.user.service.UserPhoneEncryptService;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 手机号码分词加密ServiceImpl
 * @since 2023/9/12
 */
@Service
public class UserPhoneEncryptServiceImpl implements UserPhoneEncryptService {

    @Value("amusing.phone.key.password")
    private String phoneKeyPassword;

    @Resource
    private UserPhoneEncryptMapper userPhoneEncryptMapper;

    @Override
    public Integer phoneKeywords(String userId, String phone) {
        String keywords = keywords(phone, CommConstant.FOUR);
        UserPhoneEncrypt phoneEncrypt = new UserPhoneEncrypt().setUserId(userId).setPhoneKey(keywords);
        return userPhoneEncryptMapper.insert(phoneEncrypt);
    }

    @Override
    public List<String> getUserIdsByPhone(String phone) {
        phone = encrypt(phone);
        return userPhoneEncryptMapper.getByPhoneEncrypt(phone);
    }

    private String keywords(String word, Integer len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            int end = i + len;
            String sub1 = word.substring(i, end);
            sb.append(encrypt(sub1));
            if (end == word.length()) {
                break;
            }
        }
        return sb.toString();
    }

    @Override
    public String encrypt(String val) {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(),
                phoneKeyPassword.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        return aes.encryptBase64(val);
    }

    @Override
    public String decrypt(String val) {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(),
                phoneKeyPassword.getBytes()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
        return aes.decryptStr(val);
    }

    @Override
    public String phoneHandle(String phone) {
        if (StringUtils.isBlank(phone)) {
            return "";
        }
        phone = decrypt(phone);
        return PhoneUtil.hideBetween(phone).toString();
    }

}
