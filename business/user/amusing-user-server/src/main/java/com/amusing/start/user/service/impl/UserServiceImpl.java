package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.mapper.UserInfoMapper;
import com.amusing.start.user.service.IMenuService;
import com.amusing.start.user.service.IUserService;
import com.amusing.start.utils.TokenUtils;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 2022/10/2.
 *
 * @author lvqingyu
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private AccountInfoMapper accountInfoMapper;

    @Value("${user.worker}")
    private Long workerId;

    @Value("${user.dataCenter}")
    private Long dataCenterId;

    private final PasswordEncoder passwordEncoder;

    private final IMenuService menuService;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, IMenuService menuService) {
        this.passwordEncoder = passwordEncoder;
        this.menuService = menuService;
    }

    @Override
    public Boolean registerCheck(String name, String phone) throws CustomException {
        if (StringUtils.isNotBlank(name)) {
            return userInfoMapper.getByName(name) != null;
        }
        if (StringUtils.isNotBlank(phone)) {
            return userInfoMapper.getByPhone(phone) != null;
        }
        throw new CustomException(ErrorCode.PARAMETER_ERR);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean register(RegisterDto dto) throws CustomException {
        String userName = dto.getUserName();
        if (userInfoMapper.getByName(userName) != null) {
            throw new CustomException(ErrorCode.NAME_EXISTS);
        }
        String phone = dto.getPhone();
        if (userInfoMapper.getByPhone(phone) != null) {
            throw new CustomException(ErrorCode.PHONE_EXISTS);
        }
        Long currentTime = System.currentTimeMillis();
        String userId = IdUtil.createSnowflake(workerId, dataCenterId).nextIdStr();
        String password = passwordEncoder.encode(dto.getPassword());
        UserInfo userInfo = UserInfo.builder()
                .id(userId)
                .userName(dto.getUserName())
                .password(password)
                .secret(IdUtil.fastUUID())
                .phone(dto.getPhone())
                .sources(dto.getSources())
                .status(UserStatus.VALID.getKey())
                .isDel(YesOrNo.YES.getKey())
                .createBy(userId)
                .createTime(currentTime)
                .updateBy(userId)
                .updateTime(currentTime)
                .build();
        Integer insert = userInfoMapper.insert(userInfo);
        if (insert == null || insert <= 0) {
            throw new CustomException(ErrorCode.OPERATION_ERR);
        }

        AccountInfo accountInfo = AccountInfo.builder()
                .userId(userId)
                .mainAmount(BigDecimal.ZERO)
                .giveAmount(BigDecimal.ZERO)
                .frozenAmount(BigDecimal.ZERO)
                .vipLevel(UserConstant.ZERO)
                .createBy(userId)
                .createTime(currentTime)
                .updateBy(userId)
                .updateTime(currentTime)
                .build();
        insert = accountInfoMapper.insert(accountInfo);
        if (insert == null || insert <= 0) {
            throw new CustomException(ErrorCode.OPERATION_ERR);
        }
        return UserConstant.TRUE;
    }

    @Override
    public TokenVo login(LoginDto dto) throws CustomException {
        UserInfo userInfo = userInfoMapper.getByName(dto.getUserName());
        if (userInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        boolean matches = passwordEncoder.matches(dto.getPassword(), dto.getPassword());
        if (!matches) {
            throw new CustomException(ErrorCode.LOGIN_ERR);
        }
        return generateToken(userInfo, "");
    }

    @Override
    public TokenVo refresh(String token) throws CustomException {
        Map<String, Claim> claimMap = TokenUtils.getClaims(token);
        if (CollectionUtil.isEmpty(claimMap)) {
            throw new CustomException(ErrorCode.TOKEN_ERR);
        }
        String userId = TokenUtils.getUserId(claimMap);
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(ErrorCode.TOKEN_ERR);
        }
        UserInfo userInfo = userInfoMapper.getById(userId);
        if (userInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return generateToken(userInfo, token);
    }

    private TokenVo generateToken(UserInfo userInfo, String refreshToken) {
        Date currentTime = new Date();
        String id = userInfo.getId();
        String secret = userInfo.getSecret();
        DateTime tokenExpiresTime = DateUtil.offsetSecond(currentTime, UserConstant.TOKEN_EXPIRES_TIME);
        List<Integer> roleIdList = menuService.getRoleIds(userInfo.getId());
        Integer[] roleIds;
        if (CollectionUtil.isEmpty(roleIdList)) {
            roleIds = new Integer[0];
        } else {
            roleIds = roleIdList.toArray(new Integer[0]);
        }
        String token = TokenUtils.generateToken(id, roleIds, secret, tokenExpiresTime);
        if (StringUtils.isBlank(refreshToken)) {
            tokenExpiresTime = DateUtil.offsetSecond(currentTime, UserConstant.REFRESH_TOKEN_EXPIRES_TIME);
            refreshToken = TokenUtils.generateToken(id, new Integer[0], secret, tokenExpiresTime);
        }
        return TokenVo.builder().token(token).refreshToken(refreshToken).build();
    }

}
