package com.amusing.start.user.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.client.output.AccountOutput;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.constant.UserConstant;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.entity.vo.user.UserDetailVo;
import com.amusing.start.user.entity.vo.user.UserSimpleResp;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.service.AccountService;
import com.amusing.start.user.service.IRoleService;
import com.amusing.start.user.service.UserPhoneEncryptService;
import com.amusing.start.user.service.UserService;
import com.amusing.start.user.utils.PageUtil;
import com.amusing.start.utils.TokenUtils;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Lv.QingYu
 * @description: 用户服务业务层
 * @since 2023/9/20
 */
@Slf4j
@Component
public class UserBusiness {

    private final UserService userService;

    private final AccountService accountService;

    private final UserPhoneEncryptService userPhoneEncryptService;

    private final IRoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserBusiness(UserService userService,
                        AccountService accountService,
                        UserPhoneEncryptService userPhoneEncryptService,
                        IRoleService roleService,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.accountService = accountService;
        this.userPhoneEncryptService = userPhoneEncryptService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param dto 账户、密码信息
     * @return Token信息
     * @description: 用户登录
     */
    public TokenVo login(LoginDto dto) {
        UserInfo userInfo = getUserInfoByName(dto.getUserName());
        boolean matches = passwordEncoder.matches(dto.getPassword(), userInfo.getPassword());

        return generateToken(userInfo, "");
    }

    /**
     * @param token token
     * @return token信息
     * @description: token刷新
     */
    public TokenVo refresh(String token) {
        // 根据token获取用户信息
        Map<String, Claim> claimMap = TokenUtils.getClaims(token);
        // 获取用户ID
        String userId = TokenUtils.getUserId(claimMap);
        // 根据ID获取用户信息
        UserInfo userInfo = getUserInfoById(userId, UserStatus.VALID.getKey());
        // 生成Token
        return generateToken(userInfo, token);
    }

    /**
     * @param name  用户名（非必填）
     * @param phone 手机号（非必填）
     * @param page  页码（必填）
     * @param size  每页数量（必填）
     * @return 用户基础信息列表
     * @description: 用户基础信息分页
     */
    public IPage<UserSimpleResp> getUserInfoPage(String name, String phone, Integer page, Integer size) {
        // 手机号码搜索
        List<String> userIdList = null;
        if (StringUtils.isNotBlank(phone)) {
            userIdList = userPhoneEncryptService.getUserIdsByPhone(phone);
            if (CollectionUtil.isEmpty(userIdList)) {
                return new Page<>();
            }
        }
        // 分页查询
        Page<UserInfo> of = userService.getPage(name, userIdList, page, size);
        // 类型转换
        Page<UserSimpleResp> simpleRespPage = PageUtil.transform(of, UserSimpleResp.class);
        List<UserSimpleResp> records = simpleRespPage.getRecords();
        if (CollectionUtil.isEmpty(records)) {
            return simpleRespPage;
        }
        // 手机号码脱敏
        for (UserSimpleResp userListVo : records) {
            userListVo.setPhone(userPhoneEncryptService.phoneHandle(userListVo.getPhone()));
        }
        return simpleRespPage;
    }

    /**
     * @param userId 用户ID
     * @param status 状态 {@link com.amusing.start.user.enums.UserStatus}
     * @return 用户基础信息
     * @description: 获取用户基础信息
     */
    public UserInfo getUserInfoById(String userId, Integer status) {
        UserInfo userInfo = userService.getById(userId, status);
        return phoneHandle(userInfo);
    }

    /**
     * @param name 用户名
     * @return 用户基础信息
     * @description: 获取用户基础信息
     */
    public UserInfo getUserInfoByName(String name) {
        UserInfo userInfo = userService.getByName(name);
        return phoneHandle(userInfo);
    }

    /**
     * @param name 用户名
     * @return true:存在 false:不存在
     * @description: 判断用户名是否已存在
     */
    public Boolean userNameExist(String name) {
        Integer count = userService.nameExist(name);
        return count != null && count > 0;
    }

    /**
     * @param phone 手机号
     * @return true:存在 false:不存在
     * @description: 判断手机号是否已存在
     */
    public Boolean userPhoneExist(String phone) {
        phone = userPhoneEncryptService.encrypt(phone);
        Integer count = userService.phoneExist(phone);
        return count != null && count > 0;
    }

    /**
     * @param userId 用户ID
     * @return 用户详细信息
     * @description: 获取用户详细信息
     */
    public UserDetailVo getUserInfoDetail(String userId) {
        // 用户基础信息
        UserInfo userInfo = getUserInfoById(userId, UserStatus.VALID.getKey());
        // 用户账户信息
        AccountInfo accountInfo = accountService.getByIdLock(userId);
        userInfo = phoneHandle(userInfo);
        return new UserDetailVo()
                .setUId(userInfo.getId())
                .setName(userInfo.getName())
                .setPhone(userInfo.getPhone())
                .setSources(userInfo.getSources())
                .setStatus(userInfo.getStatus());
    }

    /**
     * @param userId   用户ID
     * @param updateBy 执行人ID
     * @return true:成功 false:失败
     * @description: 用户注销
     */
    public Boolean logOffUserInfo(String userId, String updateBy) {
        UserInfo userInfo = getUserInfoById(userId, UserStatus.VALID.getKey());
        Boolean admin = roleService.isAdmin(userId);
        UserInfo delInfo = new UserInfo();
        delInfo.setId(userId);
        delInfo.setIsDel(YesOrNo.NO.getKey());
        delInfo.setUpdateBy(updateBy);
        delInfo.setUpdateTime(System.currentTimeMillis());
        Integer update = userService.update(delInfo);
        return Boolean.TRUE;
    }

    /**
     * @param userId 用户ID
     * @return 账户信息
     * @description: 获取账号信息
     */
    public AccountOutput getAccountOutputByUId(String userId) {
        AccountInfo account = accountService.getByIdLock(userId);
        return new AccountOutput().setUserId(userId)
                .setMainAmount(account.getMainAmount())
                .setGiveAmount(account.getGiveAmount())
                .setFrozenAmount(account.getFrozenAmount())
                .setVipLevel(account.getVipLevel());
    }

    /**
     * @param userId 用户ID
     * @param amount 扣减金额
     * @return true：成功 false：失败
     * @description: 主账号金额扣减
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean payment(String userId, Integer amount) {
        Integer mainAmount = accountService.getById(userId).getMainAmount();
        // 账户金额是否充足
        if (mainAmount == null || amount > mainAmount) {
            log.warn("[payment]-account balance! userId:{}, paymentAmount:{}, mainAmount:{}", userId, amount, mainAmount);
            throw new CustomException(UserErrorCode.ACCOUNT_INSUFFICIENT);
        }
        Integer result = accountService.updateMainAccount(userId, mainAmount, amount);
        // 更新结果校验
        if (result == null || result <= CommConstant.ZERO) {
            log.warn("[payment]-update account fail! userId:{}, paymentAmount:{}, mainAmount:{}", userId, amount, mainAmount);
            throw new CustomException(UserErrorCode.PAY_ERR);
        }
        return Boolean.TRUE;
    }


    /**
     * @param userInfo 用户基础信息
     * @return 用户基础信息
     * @description: 手机号码脱敏
     */
    private UserInfo phoneHandle(UserInfo userInfo) {
        if (userInfo != null) {
            userInfo.setPhone(userPhoneEncryptService.phoneHandle(userInfo.getPhone()));
        }
        return userInfo;
    }

    /**
     * @param userInfo     用户基础信息
     * @param refreshToken 刷新Token
     * @return Token信息
     * @description: 生成Token
     */
    private TokenVo generateToken(UserInfo userInfo, String refreshToken) {
        Date currentTime = new Date();
        String id = userInfo.getId();
        String secret = userInfo.getSecret();
        DateTime tokenExpiresTime = DateUtil.offsetSecond(currentTime, UserConstant.TOKEN_EXPIRES_TIME);
        List<Integer> roleIdList = roleService.getRoleIds(userInfo.getId());
        Integer[] roleIds;
        if (CollectionUtil.isEmpty(roleIdList)) {
            roleIds = new Integer[0];
        } else {
            roleIds = roleIdList.toArray(new Integer[0]);
        }
        Integer adminRoleId = roleService.getAdminRoleId();
        boolean isAdmin = false;
        if (CollectionUtil.isNotEmpty(roleIdList) && adminRoleId != null && roleIdList.contains(adminRoleId)) {
            isAdmin = true;
        }
        String token = TokenUtils.generateToken(id, roleIds, isAdmin, secret, tokenExpiresTime);
        if (StringUtils.isBlank(refreshToken)) {
            tokenExpiresTime = DateUtil.offsetSecond(currentTime, UserConstant.REFRESH_TOKEN_EXPIRES_TIME);
            refreshToken = TokenUtils.generateToken(id, new Integer[0], isAdmin, secret, tokenExpiresTime);
        }
        return TokenVo.builder().token(token).refreshToken(refreshToken).build();
    }

}
