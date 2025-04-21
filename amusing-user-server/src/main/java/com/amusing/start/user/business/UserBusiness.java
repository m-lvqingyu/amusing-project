package com.amusing.start.user.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.PageResult;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.pojo.UserPhoneEncrypt;
import com.amusing.start.user.entity.request.account.AdminAccountDetailRequest;
import com.amusing.start.user.entity.request.user.AdminUserPageRequest;
import com.amusing.start.user.entity.response.AdminUserPageResponse;
import com.amusing.start.user.enums.RegisterType;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.service.*;
import com.amusing.start.user.utils.IdCustomUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @since 2024/10/25
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserBusiness {

    private final UserService userService;

    private final AccountService accountService;

    private final EncryptService encryptService;

    private final UserPhoneEncryptService userPhoneEncryptService;

    private final PasswordEncoder passwordEncoder;

    private final RBloomFilter<String> nameBloomFilter;

    private final RBloomFilter<String> phoneBloomFilter;

    private final RedissonClient redissonClient;

    private final IdCustomUtils idCustomUtils;

    private final SmsService smsService;

    private final UserRoleService userRoleService;

    /**
     * 获取用户列表
     *
     * @param request 查询条件
     * @return 用户列表
     */
    public PageResult<AdminUserPageResponse> userPage(AdminUserPageRequest request) {
        Page<UserInfo> paged = userService.page(request.getName(),
                request.getPhone(),
                request.getSources(),
                request.getStatus(),
                request.getPage(),
                request.getSize());
        PageResult<AdminUserPageResponse> finalPage = new PageResult<>(paged.getPages(),
                paged.getTotal(),
                paged.getSize(),
                paged.getCurrent(),
                new ArrayList<>());
        List<UserInfo> userInfoList = paged.getRecords();
        if (CollectionUtil.isEmpty(userInfoList)) {
            return finalPage;
        }
        List<AdminUserPageResponse> list = userInfoList.stream().map(i -> new AdminUserPageResponse().setId(i.getId())
                .setName(i.getName())
                .setSources(i.getSources())
                .setVersion(i.getVersion())
                .setStatus(i.getStatus())
                .setCreateTime(i.getCreateTime())).collect(Collectors.toList());
        finalPage.setList(list);
        return finalPage;
    }

    /**
     * 用户名是否已经存在
     *
     * @param name 用户名
     */
    public void nameExist(String name) {
        if (!nameBloomFilter.contains(name)) {
            return;
        }
        UserInfo userInfo = userService.getByName(name);
        if (userInfo == null) {
            return;
        }
        nameBloomFilter.add(name);
        throw new CustomException(UserErrorCode.NAME_EXISTS);
    }

    /**
     * 用户名是否已经存在
     *
     * @param name   用户名
     * @param userId 用户ID
     */
    public void nameExist(String name, String userId) {
        UserInfo userInfo = userService.getByName(name);
        if (userInfo != null && !userInfo.getId().equals(userId)) {
            throw new CustomException(UserErrorCode.NAME_EXISTS);
        }
    }

    /**
     * 手机号码是否已经存在
     *
     * @param phone 手机号码
     */
    public void phoneExist(String phone) {
        if (!phoneBloomFilter.contains(phone)) {
            return;
        }
        UserInfo userInfo = userService.getByPhone(encryptService.encrypt(phone));
        if (userInfo == null) {
            return;
        }
        phoneBloomFilter.add(phone);
        throw new CustomException(UserErrorCode.PHONE_EXISTS);
    }

    /**
     * 手机号码是否已经存在
     *
     * @param phone  手机号码
     * @param userId 用户ID
     */
    public void phoneExist(String phone, String userId) {
        UserInfo userInfo = userService.getByPhone(encryptService.encrypt(phone));
        if (userInfo != null && !userInfo.getId().equals(userId)) {
            throw new CustomException(UserErrorCode.PHONE_EXISTS);
        }
    }

    /**
     * 新增用户
     *
     * @param source        来源
     * @param userId        用户唯一ID
     * @param operateUserId 操作人ID
     * @param status        状态
     * @param phone         手机号码
     * @param name          用户名
     * @param password      密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterType type, String phone, String name, String password, String code) {
        String registerCode = smsService.getRegisterCode(phone);
        if (StringUtils.isBlank(registerCode)) {
            throw new CustomException(UserErrorCode.VERIFICATION_CODE_DOES_NOT_EXIST);
        }
        if (!registerCode.equals(code)) {
            throw new CustomException(UserErrorCode.VERIFICATION_CODE_ERROR);
        }
        RLock phoneLock = redissonClient.getLock("lock:rg:ph:" + phone);
        RLock nameLock = redissonClient.getLock("lock:rg:na:" + name);
        try {
            if (!phoneLock.tryLock() || !nameLock.tryLock()) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            nameExist(name);
            phoneExist(phone);
            String userId = idCustomUtils.generateUserId();
            Long currentTime = System.currentTimeMillis();
            password = passwordEncoder.encode(password);
            UserInfo userInfo = new UserInfo().setId(userId)
                    .setName(name)
                    .setPassword(password)
                    .setSecret(IdUtil.fastUUID())
                    .setPhone(encryptService.encrypt(phone))
                    .setSources(type.getKey())
                    .setVersion(0L)
                    .setStatus(UserStatus.VALID.getKey())
                    .setIsDel(YesOrNo.YES.getKey())
                    .setCreateBy(userId)
                    .setCreateTime(currentTime)
                    .setUpdateBy(userId)
                    .setUpdateTime(currentTime);
            if (userService.insert(userInfo) <= CommConstant.ZERO) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            // 保存用户账户信息
            AccountInfo accountInfo = new AccountInfo()
                    .setUserId(userId)
                    .setMainAmount(CommConstant.ZERO)
                    .setFrozenAmount(CommConstant.ZERO)
                    .setUpdateBy(userId)
                    .setUpdateTime(currentTime);
            accountService.save(accountInfo);
            String keywords = encryptService.keywords(phone, CommConstant.FOUR);
            UserPhoneEncrypt phoneEncrypt = new UserPhoneEncrypt().setUserId(userId).setPhoneKey(keywords);
            if (userPhoneEncryptService.save(phoneEncrypt) <= CommConstant.ZERO) {
                throw new CustomException(CommunalCode.SERVICE_ERR);
            }
            nameBloomFilter.add(name);
            phoneBloomFilter.add(phone);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("[User]-insert err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        } finally {
            nameLock.unlock();
            phoneLock.unlock();
        }
    }

    public AdminAccountDetailRequest account(String id) {
        AccountInfo accountInfo = accountService.get(id);
        return new AdminAccountDetailRequest().setUserId(accountInfo.getUserId())
                .setMainAmount(accountInfo.getMainAmount())
                .setFrozenAmount(accountInfo.getFrozenAmount());
    }

    public Boolean changePassword(String operateUserId, String userId, String password, String confirmPassword) {
        if (!password.equalsIgnoreCase(confirmPassword)) {
            throw new CustomException(UserErrorCode.PW_ERR);
        }
        return userService.changePw(userId, operateUserId, password);
    }

    public void userDetail(String userId) {
        UserInfo userInfo = userService.getById(userId, UserStatus.VALID.getKey());
        if (userInfo == null) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }
        AccountInfo accountInfo = accountService.get(userId);
        if (accountInfo == null) {
            throw new CustomException(UserErrorCode.ACCOUNT_FROZEN_ERR);
        }
        List<Integer> roleIdList = userRoleService.getRoleIdList(userId);
        
    }

}
