package com.amusing.start.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.pojo.MenuInfo;
import com.amusing.start.user.entity.pojo.RoleInfo;
import com.amusing.start.user.entity.pojo.RoleMenuInfo;
import com.amusing.start.user.enums.UserStatus;
import com.amusing.start.user.enums.YesOrNo;
import com.amusing.start.user.mapper.MenuInfoMapper;
import com.amusing.start.user.mapper.RoleInfoMapper;
import com.amusing.start.user.mapper.RoleMenuInfoMapper;
import com.amusing.start.user.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleInfoMapper roleInfoMapper;

    @Resource
    private MenuInfoMapper menuInfoMapper;

    @Resource
    private RoleMenuInfoMapper roleMenuInfoMapper;

    @Override
    public Integer add(String userId, RoleAddDto dto) throws CustomException {
        Integer id = dto.getId();
        // 修改
        if (id != null && id > 0) {
            RoleInfo byId = roleInfoMapper.getById(id);
            if (byId == null) {
                throw new CustomException(ErrorCode.ROLE_NOT_FUND);
            }
            String name = dto.getName();
            RoleInfo byName = roleInfoMapper.getByName(name);
            if (byName != null && !id.equals(byName.getId())) {
                throw new CustomException(ErrorCode.ROLE_NAME_EXIST);
            }
            String nameCode = dto.getNameCode();
            RoleInfo byCode = roleInfoMapper.getByCode(nameCode);
            if (byCode != null && !id.equals(byCode.getId())) {
                throw new CustomException(ErrorCode.ROLE_CODE_EXIST);
            }
            long millis = System.currentTimeMillis();
            RoleInfo roleInfo = RoleInfo.builder()
                    .id(id)
                    .name(name)
                    .nameCode(nameCode)
                    .description(dto.getDescription())
                    .status(dto.getStatus())
                    .updateBy(userId)
                    .updateTime(millis)
                    .build();
            Integer update = roleInfoMapper.update(roleInfo);
            if (update == null || update <= 0) {
                throw new CustomException(ErrorCode.ROLE_UPDATE_FAIL);
            }
            return id;
        }
        // 新增
        String name = dto.getName();
        RoleInfo byName = roleInfoMapper.getByName(name);
        if (byName != null) {
            throw new CustomException(ErrorCode.ROLE_NAME_EXIST);
        }
        String nameCode = dto.getNameCode();
        RoleInfo byCode = roleInfoMapper.getByCode(nameCode);
        if (byCode != null) {
            throw new CustomException(ErrorCode.ROLE_CODE_EXIST);
        }
        long millis = System.currentTimeMillis();
        RoleInfo roleInfo = RoleInfo.builder()
                .name(name)
                .nameCode(nameCode)
                .description(dto.getDescription())
                .status(dto.getStatus())
                .isDel(YesOrNo.YES.getKey())
                .isAdmin(YesOrNo.NO.getKey())
                .createBy(userId)
                .createTime(millis)
                .updateBy(userId)
                .updateTime(millis)
                .build();
        Integer insert = roleInfoMapper.insert(roleInfo);
        if (insert == null || insert <= 0) {
            throw new CustomException(ErrorCode.ROLE_UPDATE_FAIL);
        }
        return roleInfo.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean menuBind(String userId, RoleMenuBindDto dto) throws CustomException {
        Integer roleId = dto.getRoleId();
        RoleInfo byId = roleInfoMapper.getById(roleId);
        if (byId == null) {
            throw new CustomException(ErrorCode.ROLE_NOT_FUND);
        }
        List<Integer> menuIds = dto.getMenuIds();
        if (CollectionUtil.isEmpty(menuIds)) {
            roleMenuInfoMapper.invalidByRoleId(roleId);
            return true;
        }
        List<MenuInfo> infoList = menuInfoMapper.getByIds(menuIds);
        if (CollectionUtil.isEmpty(infoList) || infoList.size() != menuIds.size()) {
            throw new CustomException(ErrorCode.MENU_NOT_FOUND);
        }
        roleMenuInfoMapper.invalidByRoleId(roleId);
        long millis = System.currentTimeMillis();
        for (Integer menuId : menuIds) {
            RoleMenuInfo roleMenu = roleMenuInfoMapper.getRoleMenu(roleId, menuId);
            if (roleMenu != null) {
                roleMenu.setStatus(UserStatus.VALID.getKey());
                roleMenu.setUpdateBy(userId);
                roleMenu.setUpdateTime(millis);
                Integer update = roleMenuInfoMapper.update(roleMenu);
                if (update == null || update <= 0) {
                    throw new CustomException(ErrorCode.OPERATE_FAIL);
                }
                continue;
            }
            RoleMenuInfo info = RoleMenuInfo.builder()
                    .roleId(roleId)
                    .menuId(menuId)
                    .status(UserStatus.VALID.getKey())
                    .isDel(YesOrNo.YES.getKey())
                    .createBy(userId)
                    .createTime(millis)
                    .updateBy(userId)
                    .updateTime(millis)
                    .build();
            Integer insert = roleMenuInfoMapper.insert(info);
            if (insert == null || insert <= 0) {
                throw new CustomException(ErrorCode.OPERATE_FAIL);
            }
        }
        return true;
    }


}
