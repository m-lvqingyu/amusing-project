package com.amusing.start.auth.service.impl;

import cn.hutool.core.util.IdUtil;
import com.amusing.start.auth.constant.AuthConstant;
import com.amusing.start.auth.dto.UserCreateDto;
import com.amusing.start.auth.enums.UserDel;
import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.IRegisterService;
import com.amusing.start.auth.service.IUserService;
import com.amusing.start.client.api.UserClient;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @author lv.qingyu
 */
@Slf4j
@Service
public class RegisterServiceImpl implements IRegisterService {

    @Value("${auth.worker}")
    private Long workerId;

    @Value("${auth.dataCenter}")
    private Long dataCenterId;

    private final PasswordEncoder passwordEncoder;

    private final IUserService userService;

    private final UserClient userClient;

    @Autowired
    public RegisterServiceImpl(PasswordEncoder passwordEncoder, IUserService userService, UserClient userClient) {
        this.userService = userService;
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(value = TransactionType.BASE)
    @Override
    public String userRegister(UserCreateDto createDTO) throws AuthException {
        // 校验用户名或手机号是否已经存在
        Long count = userService.queryNotDelByNameOrPhone(createDTO.getUserName(), createDTO.getPhone());
        if (count != null && count > 0) {
            log.warn("[auth]-userRegister username:{} or phone:{} is exist!", createDTO.getUserName(), createDTO.getPhone());
            throw new AuthException(AuthCode.USER_PHONE_EXISTS);
        }
        // 保存用户基础信息
        SysUserBase userBase = build(createDTO);
        Integer result = userService.saveUser(userBase);
        Optional.ofNullable(result).filter(i -> i > AuthConstant.ZERO).orElseThrow(() -> new AuthException(AuthCode.USER_SAVE_ERROR));
        // 初始化用户账户信息
        String userId = userBase.getUserId();
        ApiResult<Boolean> initResult;
        try {
            initResult = userClient.init(userId);
            log.info("[auth]-userRegister init account userId:{}, result:{}", userId, initResult);
        } catch (Exception e) {
            log.error("[auth]-userRegister init account err! userId:{} msg:{}!", userId, Throwables.getStackTraceAsString(e));
            throw new AuthException(AuthCode.USER_SAVE_ERROR);
        }
        checkInitAccountResult(initResult);
        return userId;
    }

    private void checkInitAccountResult(ApiResult<Boolean> initResult) throws AuthException {
        Optional.ofNullable(initResult)
                .map(ApiResult::getCode)
                .filter(code -> CommCode.SUCCESS.key().intValue() == code)
                .orElseThrow(() -> new AuthException(AuthCode.USER_SAVE_ERROR));
        boolean data = initResult.getData();
        if (!data) {
            throw new AuthException(AuthCode.USER_SAVE_ERROR);
        }
    }

    private SysUserBase build(UserCreateDto createDTO) {
        Long currentTime = System.currentTimeMillis();
        String userId = IdUtil.createSnowflake(workerId, dataCenterId).nextIdStr();
        String password = passwordEncoder.encode(createDTO.getPassword());
        return SysUserBase.builder()
                .userId(userId)
                .userName(createDTO.getUserName())
                .password(password)
                .secret(IdUtil.fastUUID())
                .phone(createDTO.getPhone())
                .sources(createDTO.getSources())
                .status(UserStatus.VALID.getKey())
                .isDel(UserDel.NOT_DELETED.getKey())
                .createBy(userId)
                .createTime(currentTime)
                .updateBy(userId)
                .updateTime(currentTime)
                .build();
    }
}
