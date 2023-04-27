package com.amusing.start.user.service.impl;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.pojo.AccountInfo;
import com.amusing.start.user.entity.pojo.UserInfo;
import com.amusing.start.user.entity.vo.user.UserDetailVo;
import com.amusing.start.user.entity.vo.user.UserListVo;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.AccountInfoMapper;
import com.amusing.start.user.mapper.UserInfoMapper;

import com.amusing.start.user.service.IRoleService;
import com.amusing.start.user.service.IUserService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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

    private final IRoleService roleService;

    @Autowired
    public UserServiceImpl(IRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public IPage<UserListVo> list(String name, String phone, Integer page, Integer size) {
        Page<UserListVo> of = Page.of(page, size);
        userInfoMapper.selectList(of, name, phone);
        return of;
    }

    @Override
    public UserDetailVo detail(String userId) throws CustomException {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        AccountInfo accountInfo = accountInfoMapper.selectById(userId);
        if (accountInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return UserDetailVo.builder().uId(userInfo.getUId())
                .name(userInfo.getName()).phone(userInfo.getPhone())
                .sources(userInfo.getSources()).status(userInfo.getStatus())
                .mainAmount(accountInfo.getMainAmount())
                .giveAmount(accountInfo.getGiveAmount())
                .frozenAmount(accountInfo.getFrozenAmount())
                .vipLevel(accountInfo.getVipLevel())
                .build();
    }

    @Override
    public Boolean del(String userId, String updateBy) throws CustomException {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (userInfo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        Boolean admin = roleService.isAdmin(userId);
        if (admin != null && admin) {
            throw new CustomException(ErrorCode.ADMIN_OPERATE_FAIL);
        }
        UserInfo delInfo = new UserInfo();
        delInfo.setUId(userId);
        delInfo.setIsDel(YesOrNo.NO.getKey());
        delInfo.setUpdateBy(updateBy);
        delInfo.setUpdateTime(System.currentTimeMillis());
        Integer update = userInfoMapper.update(delInfo);
        if (update == null || update <= 0) {
            throw new CustomException(ErrorCode.OPERATE_FAIL);
        }
        return true;
    }


}
