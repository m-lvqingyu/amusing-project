package com.amusing.start.user.controller.admin.user;

import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import com.amusing.start.result.PageResult;
import com.amusing.start.user.business.RoleMenuBusiness;
import com.amusing.start.user.business.UserBusiness;
import com.amusing.start.user.entity.request.account.AdminAccountDetailRequest;
import com.amusing.start.user.entity.request.user.AdminUserPageRequest;
import com.amusing.start.user.entity.request.user.AdminUserPwRequest;
import com.amusing.start.user.entity.request.user.AdminUserRoleBindRequest;
import com.amusing.start.user.entity.response.AdminUserPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Lv.QingYu
 * @since 2023/9/8
 */
@Tag(name = "员工管理")
@Validated
@RestController
@RequestMapping("user/admin/user")
public class AdminUserController extends BaseController {

    private final UserBusiness userBusiness;

    private final RoleMenuBusiness roleMenuBusiness;

    public AdminUserController(HttpServletRequest request,
                               UserBusiness userBusiness,
                               RoleMenuBusiness roleMenuBusiness) {
        super(request);
        this.userBusiness = userBusiness;
        this.roleMenuBusiness = roleMenuBusiness;
    }

    @Operation(summary = "员工列表(分页)", description = "需要登录后访问")
    @PostMapping("page")
    public ApiResult<PageResult<AdminUserPageResponse>> page(@Valid @RequestBody AdminUserPageRequest request) {
        return ApiResult.ok(userBusiness.userPage(request));
    }

    @Operation(summary = "员工账户详情", description = "需要登录后访问")
    @Parameter(name = "id", description = "员工ID")
    @GetMapping("account/{id}")
    public ApiResult<AdminAccountDetailRequest> account(@PathVariable("id") String id) {
        return ApiResult.ok(userBusiness.account(id));
    }

    @Operation(summary = "员工绑定角色", description = "需要登录后访问")
    @PostMapping("bind/role")
    public ApiResult<Boolean> bindRole(@Valid @RequestBody AdminUserRoleBindRequest request) {
        return ApiResult.ok(roleMenuBusiness.bindRole(getUserId(), request.getUserId(), request.getRoleIds()));
    }

    @Operation(summary = "员工修改密码", description = "需要登录后访问")
    @PostMapping("pw")
    public ApiResult<Boolean> pw(@Valid @RequestBody AdminUserPwRequest request) {
        return ApiResult.ok(userBusiness.changePassword(getUserId(),
                request.getUserId(),
                request.getPassword(),
                request.getConfirmPassword()));
    }

}
