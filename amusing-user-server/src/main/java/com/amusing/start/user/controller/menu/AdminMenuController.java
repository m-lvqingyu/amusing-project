package com.amusing.start.user.controller.menu;

import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.business.RoleMenuBusiness;
import com.amusing.start.user.entity.request.menu.MenuAddRequest;
import com.amusing.start.user.entity.request.menu.MenuEditRequest;
import com.amusing.start.user.entity.response.MenuResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Lv.QingYu
 * @since 2024/8/7
 */
@Tag(name = "菜单管理")
@Validated
@RestController
@RequestMapping("user/admin/menu")
public class AdminMenuController extends BaseController {

    private final RoleMenuBusiness roleMenuBusiness;

    public AdminMenuController(HttpServletRequest request, RoleMenuBusiness roleMenuBusiness) {
        super(request);
        this.roleMenuBusiness = roleMenuBusiness;
    }

    @Operation(summary = "菜单新增", description = "需要登录后访问")
    @PostMapping("save")
    public ApiResult<Boolean> save(@Valid @RequestBody MenuAddRequest request) {
        roleMenuBusiness.menuAdd(getUserId(), request);
        return ApiResult.ok();
    }

    @Operation(summary = "菜单修改", description = "需要登录后访问")
    @PostMapping("edit")
    public ApiResult<Boolean> edit(@Valid @RequestBody MenuEditRequest request) {
        roleMenuBusiness.menuEdit(getUserId(), request);
        return ApiResult.ok();
    }

    @Operation(summary = "菜单删除", description = "需要登录后访问")
    @Parameter(name = "id", description = "菜单ID")
    @DeleteMapping("del/{id}")
    public ApiResult<Boolean> del(@PathVariable(value = "id") Integer id) {
        roleMenuBusiness.menuDel(getUserId(), id);
        return ApiResult.ok();
    }

    @Operation(summary = "菜单列表(树形结构)", description = "需要登录后访问")
    @GetMapping("tree")
    public ApiResult<List<MenuResponse>> tree() {
        return ApiResult.ok(roleMenuBusiness.getMenuTree(getUserId()));
    }

}
