package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.pojo.*;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.RoleInfoMapper;
import com.amusing.start.user.service.RoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lv.QingYu
 * @since 2021/09/21
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Override
    public Page<RoleInfo> page(String name, String code, Integer size, Integer page) {
        LambdaQueryWrapper<RoleInfo> query = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) query.eq(RoleInfo::getName, name);
        if (StringUtils.isNotBlank(code)) query.eq(RoleInfo::getCode, code);
        return roleInfoMapper.selectPage(Page.of(page, size), query);
    }

    @Override
    public void nameExist(Integer id, String name) {
        roleExist(id, RoleInfo::getName, name);
    }

    @Override
    public void nameExist(String name) {
        roleExist(RoleInfo::getName, name);
    }

    @Override
    public void codeExist(Integer id, String code) {
        roleExist(id, RoleInfo::getCode, code);
    }

    @Override
    public void codeExist(String code) {
        roleExist(RoleInfo::getCode, code);
    }

    @Override
    public void roleExist(Integer id) {
        LambdaQueryWrapper<RoleInfo> query = new LambdaQueryWrapper<>();
        query.eq(RoleInfo::getId, id);
        query.eq(RoleInfo::getIsDel, YesOrNo.YES.getKey());
        if (roleInfoMapper.selectOne(query) == null) {
            throw new CustomException(UserErrorCode.ROLE_NOT_FUND);
        }
    }

    @Override
    public void update(RoleInfo roleInfo) {
        if (roleInfo.getId() == null) {
            throw new CustomException(CommunalCode.PARAMETER_ERR);
        }
        if (roleInfoMapper.updateById(roleInfo) <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public void insert(RoleInfo roleInfo) {
        if (roleInfoMapper.insert(roleInfo) <= 0) {
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

    @Override
    public Integer adminRoleId() {
        LambdaQueryWrapper<RoleInfo> query = new LambdaQueryWrapper<RoleInfo>()
                .eq(RoleInfo::getIsAdmin, YesOrNo.YES.getKey())
                .eq(RoleInfo::getStatus, YesOrNo.YES.getKey())
                .eq(RoleInfo::getIsDel, YesOrNo.YES.getKey());
        RoleInfo roleInfo = roleInfoMapper.selectOne(query);
        if (roleInfo == null) {
            throw new CustomException(UserErrorCode.ROLE_NOT_FUND);
        }
        return roleInfo.getId();
    }

    private void roleExist(SFunction<RoleInfo, ?> column, Object val) {
        List<RoleInfo> infoList = get(column, val);
        if (CollectionUtil.isNotEmpty(infoList) && infoList.size() != 0) {
            throw new CustomException(UserErrorCode.ROLE_EXIST);
        }
    }

    private void roleExist(Integer roleId, SFunction<RoleInfo, ?> column, Object val) {
        List<RoleInfo> infoList = get(column, val);
        if (CollectionUtil.isEmpty(infoList)) {
            return;
        }
        List<Integer> idList = infoList.stream().map(RoleInfo::getId).collect(Collectors.toList());
        if (!idList.contains(roleId)) {
            throw new CustomException(UserErrorCode.ROLE_EXIST);
        }
    }

    private List<RoleInfo> get(SFunction<RoleInfo, ?> column, Object val) {
        LambdaQueryWrapper<RoleInfo> query = new LambdaQueryWrapper<>();
        query.eq(column, val);
        query.eq(RoleInfo::getIsDel, YesOrNo.YES.getKey());
        return roleInfoMapper.selectList(query);
    }

}
