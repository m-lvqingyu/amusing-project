package com.amusing.start.auth.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.auth.dto.UserCreateDTO;
import com.amusing.start.auth.enums.UserDel;
import com.amusing.start.auth.enums.UserStatus;
import com.amusing.start.auth.exception.AuthException;
import com.amusing.start.auth.mapper.SysUserBaseMapper;
import com.amusing.start.auth.pojo.SysUserBase;
import com.amusing.start.auth.pojo.SysUserBaseExample;
import com.amusing.start.auth.service.UserService;
import com.amusing.start.result.ApiCode;
import com.amusing.start.result.ApiResult;
import org.springframework.beans.BeanUtils;
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
    public ApiResult create(UserCreateDTO createDTO) throws AuthException {
        Long count = countUsername(createDTO.getUserName());
        if (count != null && count > 0) {
            throw new AuthException(ApiCode.USER_SAVE_ERROR);
        }
        count = countPhone(createDTO.getPhone());
        if (count != null && count > 0) {
            throw new AuthException(ApiCode.USER_SAVE_ERROR);
        }
        SysUserBase userBase = build(createDTO);
        sysUserBaseMapper.insertSelective(userBase);
        return ApiResult.ok();
    }

    private Long countUsername(String username) {
        SysUserBaseExample example = new SysUserBaseExample();
        example.createCriteria().andUserNameEqualTo(username).andIsDelEqualTo(UserDel.NOT_DELETED.getKey());
        return sysUserBaseMapper.countByExample(example);
    }

    private Long countPhone(String phone) {
        SysUserBaseExample example = new SysUserBaseExample();
        example.createCriteria().andPhoneEqualTo(phone).andIsDelEqualTo(UserDel.NOT_DELETED.getKey());
        return sysUserBaseMapper.countByExample(example);
    }

    private SysUserBase build(UserCreateDTO createDTO) {
        SysUserBase sysUserBase = new SysUserBase();
        BeanUtils.copyProperties(createDTO, sysUserBase);
        String userId = generateUserId();
        sysUserBase.setUserId(userId);
        String password = passwordEncoder.encode(createDTO.getPassword());
        sysUserBase.setPassword(password);
        sysUserBase.setSecret(IdUtil.fastUUID());
        sysUserBase.setStatus(UserStatus.VALID.getKey());
        sysUserBase.setIsDel(UserDel.NOT_DELETED.getKey());
        sysUserBase.setCreateBy(userId);
        sysUserBase.setUpdateBy(userId);
        Date currentTime = new Date();
        sysUserBase.setCreateTime(currentTime);
        sysUserBase.setUpdateTime(currentTime);
        return sysUserBase;
    }

    private String generateUserId() {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
        return snowflake.nextIdStr();
    }

}
