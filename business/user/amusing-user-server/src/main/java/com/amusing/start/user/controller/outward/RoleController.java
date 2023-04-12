package com.amusing.start.user.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward/role")
public class RoleController extends BaseController {

    private IRoleService roleService;

    @Autowired
    public RoleController(HttpServletRequest request, IRoleService roleService) {
        super(request);
        this.roleService = roleService;
    }

    @PostMapping("add")
    public ApiResult<Integer> add(@Valid @RequestBody RoleAddDto dto) throws CustomException {
        return ApiResult.ok(roleService.add(getUserId(), dto));
    }

    @PostMapping("menu/bind")
    public ApiResult<Boolean> bind(@Valid @RequestBody RoleMenuBindDto dto) throws CustomException {
        return ApiResult.ok(roleService.menuBind(getUserId(), dto));
    }

}
