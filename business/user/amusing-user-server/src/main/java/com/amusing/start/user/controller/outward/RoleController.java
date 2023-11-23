package com.amusing.start.user.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.dto.RoleAddDto;
import com.amusing.start.user.entity.dto.RoleMenuBindDto;
import com.amusing.start.user.entity.vo.RoleInfoVo;
import com.amusing.start.user.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @description: 角色管理
 * @since 2023/4/12
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("user/out/role")
public class RoleController extends BaseController {

    private final IRoleService roleService;

    @Autowired
    public RoleController(HttpServletRequest request, IRoleService roleService) {
        super(request);
        this.roleService = roleService;
    }

    @ApiOperation(value = "角色列表", notes = "角色列表分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "编码", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "size", value = "每页条目", dataType = "Integer", defaultValue = "10", required = true)
    })
    @GetMapping("list")
    public ApiResult<IPage<RoleInfoVo>> list(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "code", required = false) String code,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ApiResult.ok(roleService.list(name, code, page, size));
    }

    @ApiOperation(value = "角色新增或修改", notes = "角色新增或修改")
    @PostMapping("add")
    public ApiResult<Integer> add(@Valid @RequestBody RoleAddDto dto) throws CustomException {
        return ApiResult.ok(roleService.add(getUserId(), dto));
    }

    @ApiOperation(value = "角色菜单绑定", notes = "角色菜单绑定")
    @PostMapping("menu/bind")
    public ApiResult<Boolean> bind(@Valid @RequestBody RoleMenuBindDto dto) throws CustomException {
        return ApiResult.ok(roleService.menuBind(getUserId(), dto));
    }

}
