package com.amusing.start.user.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.amusing.start.user.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by 2023/04/11.
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward/role")
public class RoleOutwardController extends BaseController {

    private final IRoleService roleService;

    @Autowired
    public RoleOutwardController(HttpServletRequest request, IRoleService roleService) {
        super(request);
        this.roleService = roleService;
    }

    @GetMapping("list")
    public ApiResult<IPage<RoleInfoVo>> list(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "code", required = false) String code,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ApiResult.ok(roleService.list(name, code, page, size));
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
