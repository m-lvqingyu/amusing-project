package com.amusing.start.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.CommCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.service.AccountService;
import com.amusing.start.user.service.UserPhoneEncryptService;
import com.amusing.start.user.service.UserService;
import com.amusing.start.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Lv.QingYu
 * @description:
 * @since 2023/11/20
 */
@Service
public class LoginServiceImpl implements LoginService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final UserPhoneEncryptService userPhoneEncryptService;

    private final AccountService accountService;

    @Autowired
    public LoginServiceImpl(UserService userService,
                            PasswordEncoder passwordEncoder,
                            UserPhoneEncryptService userPhoneEncryptService,
                            AccountService accountService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userPhoneEncryptService = userPhoneEncryptService;
        this.accountService = accountService;
    }

    @Value("${user.worker}")
    private Long workerId;

    @Value("${user.dataCenter}")
    private Long dataCenterId;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean register(Integer registerType, RegisterDto dto) throws CustomException {
        Integer count = userService.nameExist(dto.getUserName());
        if (count != null && count > CommConstant.ZERO) {
            throw new CustomException(UserErrorCode.NAME_EXISTS);
        }
        count = userService.phoneExist(userPhoneEncryptService.encrypt(dto.getPhone()));
        if (count != null && count > CommConstant.ZERO) {
            throw new CustomException(UserErrorCode.PHONE_EXISTS);
        }
        // 保存用户基础信息
        Long currentTime = System.currentTimeMillis();
        String userId = IdUtil.createSnowflake(workerId, dataCenterId).nextIdStr();
        String password = passwordEncoder.encode(dto.getPassword());
        UserInfo userInfo = new UserInfo().setId(userId)
                .setName(dto.getUserName())
                .setPassword(password)
                .setSecret(IdUtil.fastUUID())
                .setPhone(userPhoneEncryptService.encrypt(dto.getPhone()))
                .setSources(registerType)
                .setStatus(UserStatus.VALID.getKey())
                .setIsDel(YesOrNo.YES.getKey())
                .setCreateBy(userId)
                .setCreateTime(currentTime)
                .setUpdateBy(userId)
                .setUpdateTime(currentTime);
        Optional.ofNullable(userService.insert(userInfo))
                .filter(i -> i > CommConstant.ZERO)
                .orElseThrow(() -> new CustomException(CommCode.SERVICE_ERR));
        // 保存用户账户信息
        AccountInfo accountInfo = new AccountInfo()
                .setUserId(userId)
                .setMainAmount(CommConstant.ZERO)
                .setGiveAmount(CommConstant.ZERO)
                .setFrozenAmount(CommConstant.ZERO)
                .setVipLevel(CommConstant.ZERO)
                .setCreateBy(userId)
                .setCreateTime(currentTime)
                .setUpdateBy(userId)
                .setUpdateTime(currentTime);
        Optional.ofNullable(accountService.insert(accountInfo))
                .filter(i -> i > CommConstant.ZERO)
                .orElseThrow(() -> new CustomException(CommCode.SERVICE_ERR));
        Optional.ofNullable(userPhoneEncryptService.phoneKeywords(userId, dto.getPhone()))
                .filter(i -> i > CommConstant.ZERO)
                .orElseThrow(() -> new CustomException(CommCode.SERVICE_ERR));
        return Boolean.TRUE;
    }
}
