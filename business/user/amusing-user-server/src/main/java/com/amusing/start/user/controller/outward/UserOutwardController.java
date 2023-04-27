package com.amusing.start.user.controller.outward;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.log.LogOutput;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.annotation.RequestLimit;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.entity.vo.user.UserDetailVo;
import com.amusing.start.user.entity.vo.user.UserListVo;
import com.amusing.start.user.service.ILoginService;
import com.amusing.start.user.service.IUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by 2023/04/12.
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward/info")
@Validated
@Api(tags = "用户管理")
public class UserOutwardController extends BaseController {

    private final IUserService userService;

    private final ILoginService loginService;

    public UserOutwardController(HttpServletRequest request, IUserService userService, ILoginService loginService) {
        super(request);
        this.userService = userService;
        this.loginService = loginService;
    }

    @ApiOperation(value = "用户列表", notes = "用户列表分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "size", value = "每页条目", dataType = "Integer", defaultValue = "10", required = true)
    })
    @LogOutput
    @GetMapping("list")
    public ApiResult<IPage<UserListVo>> list(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "phone", required = false) String phone,
                                             @NotNull(message = "页码错误") @Min(value = 1L, message = "页码错误") @Max(value = 1000L, message = "页码错误") @RequestParam(value = "page") Integer page,
                                             @NotNull(message = "分页条数错误") @Max(value = 30L, message = "分页条数错误") @Min(value = 5L, message = "分页条数错误") @RequestParam(value = "size") Integer size) {
        return ApiResult.ok(userService.list(name, phone, page, size));
    }

    @ApiOperation(value = "用户详情", notes = "根据ID获取用户详情信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @LogOutput
    @GetMapping("detail")
    public ApiResult<UserDetailVo> detail(@NotBlank(message = "用户ID不能为空") @RequestParam("id") String id) throws CustomException {
        return ApiResult.ok(userService.detail(id));
    }

    @ApiOperation(value = "用户删除", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @LogOutput
    @DeleteMapping("del")
    public ApiResult<Boolean> del(@NotBlank(message = "用户ID不能为空") @RequestParam("id") String id) throws CustomException {
        return ApiResult.ok(userService.del(id, getUserId()));
    }

    @ApiOperation(value = "用户注册校验", notes = "用户注册-用户名与手机号校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名"),
            @ApiImplicitParam(name = "phone", value = "手机号码")
    })
    @LogOutput
    @GetMapping("register/check")
    @RequestLimit(count = 2)
    public ApiResult<Boolean> check(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "phone", required = false) String phone) throws CustomException {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(phone)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return ApiResult.ok(loginService.check(name, phone));
    }

    @ApiOperation(value = "用户注册", notes = "用户名与密码注册")
    @LogOutput
    @PostMapping("register")
    public ApiResult<Boolean> register(@Valid @RequestBody RegisterDto dto) throws CustomException {
        return ApiResult.ok(loginService.register(dto));
    }

    @ApiOperation(value = "用户登陆", notes = "用户名与密码登陆")
    @LogOutput
    @PostMapping("login")
    public ApiResult<TokenVo> login(@Valid @RequestBody LoginDto dto) throws CustomException {
        return ApiResult.ok(loginService.login(dto));
    }

    @ApiOperation(value = "用户Token刷新", notes = "Token失效，刷新Token")
    @LogOutput
    @GetMapping("refresh")
    public ApiResult<TokenVo> refresh(@NotBlank(message = "token不能为空") @RequestParam("token") String token) throws CustomException {
        return ApiResult.ok(loginService.refresh(token));
    }

}
