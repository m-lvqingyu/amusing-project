package com.amusing.start.auth.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.auth.dto.UserCreateDto;
import com.amusing.start.auth.enums.UserDel;
import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.exception.code.AuthCode;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.service.UserService;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${auth.worker}")
    private Long workerId;

    @Value("${auth.dataCenter}")
    private Long dataCenterId;

    @Resource
    private SysUserBaseMapper sysUserBaseMapper;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResult create(String executorUserId, UserCreateDto createDTO) throws AuthException {
        Long count = sysUserBaseMapper.selectNotDelByName(createDTO.getUserName());
        if (count != null && count > 0) {
            throw new AuthException(AuthCode.USER_SAVE_ERROR);
        }
        count = sysUserBaseMapper.selectNotDelByPhone(createDTO.getPhone());
        if (count != null && count > 0) {
            throw new AuthException(AuthCode.USER_SAVE_ERROR);
        }
        SysUserBase userBase = build(executorUserId, createDTO);
        sysUserBaseMapper.insertSelective(userBase);
        return ApiResult.ok();
    }

    private SysUserBase build(String executorUserId, UserCreateDto createDTO) {
        Date currentTime = new Date();
        String userId = generateUserId();
        String password = passwordEncoder.encode(createDTO.getPassword());
        SysUserBase sysUserBase = SysUserBase.builder()
                .userId(userId)
                .userName(createDTO.getUserName())
                .password(password)
                .secret(IdUtil.fastUUID())
                .phone(createDTO.getPhone())
                .sources(createDTO.getSources())
                .dingTalkId(createDTO.getDingTalkId())
                .companyWeChatId(createDTO.getCompanyWeChatId())
                .status(UserStatus.VALID.getKey())
                .isDel(UserDel.NOT_DELETED.getKey())
                .createBy(executorUserId)
                .createTime(currentTime)
                .updateBy(executorUserId)
                .updateTime(currentTime)
                .build();
        return sysUserBase;
    }

    private String generateUserId() {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
        return snowflake.nextIdStr();
    }

}
