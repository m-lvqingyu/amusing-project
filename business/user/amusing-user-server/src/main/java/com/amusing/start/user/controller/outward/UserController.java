package com.amusing.start.user.controller.outward;

import cn.hutool.core.util.NumberUtil;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.constant.CommConstant;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.log.LogOutput;
import com.amusing.start.result.ApiResult;
import com.amusing.start.annotation.RequestLimit;
import com.amusing.start.user.business.UserBusiness;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.entity.vo.user.UserDetailVo;
import com.amusing.start.user.entity.vo.user.UserSimpleResp;
import com.amusing.start.user.enums.RegisterType;
import com.amusing.start.user.enums.UserErrorCode;
import com.amusing.start.user.service.LoginService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * @author Lv.QingYu
 * @description: 用户管理
 * @since 2023/9/8
 */
@Api(tags = "用户管理")
@Validated
@RestController
@RequestMapping("user/out")
public class UserController extends BaseController {

    private final UserBusiness userBusiness;

    private final LoginService loginService;

    @Autowired
    public UserController(HttpServletRequest request, UserBusiness userBusiness,
                          LoginService loginService) {
        super(request);
        this.userBusiness = userBusiness;
        this.loginService = loginService;
    }

    /**
     * 用户名正则表达式
     */
    private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$";

    @ApiOperation(value = "注册-用户名校验")
    @ApiImplicitParam(name = "name", value = "用户名", required = true)
    @RequestLimit(count = 2)
    @GetMapping("register/check/name")
    public ApiResult<Boolean> checkName(@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9*@_]{4,31}$", message = "用户名格式不正确")
                                        @RequestParam(value = "name") String name) {
        return ApiResult.ok(userBusiness.userNameExist(name));
    }

    @ApiOperation(value = "注册-手机号校验")
    @ApiImplicitParam(name = "phone", value = "手机号码", required = true)
    @RequestLimit(count = 2)
    @GetMapping("register/check/phone")
    public ApiResult<Boolean> checkPhone(@Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号码格式不正确")
                                         @RequestParam(value = "phone") String phone) {
        return ApiResult.ok(userBusiness.userPhoneExist(phone));
    }

    @ApiOperation(value = "注册-PC注册")
    @LogOutput
    @PostMapping("register/web")
    public ApiResult<Boolean> webRegister(@Valid @RequestBody RegisterDto dto) {
        return ApiResult.ok(loginService.register(RegisterType.PC.getKey(), dto));
    }

    @ApiOperation(value = "登陆-PC登录")
    @LogOutput
    @PostMapping("login")
    public ApiResult<TokenVo> login(@Valid @RequestBody LoginDto dto) {
        return ApiResult.ok(userBusiness.login(dto));
    }

    @ApiOperation(value = "登录-Token刷新")
    @ApiImplicitParam(name = "token", value = "token", required = true)
    @LogOutput
    @GetMapping("login/refresh")
    public ApiResult<TokenVo> refresh(@NotBlank(message = "token不能为空") @RequestParam("token") String token) {
        return ApiResult.ok(userBusiness.refresh(token));
    }

    @ApiOperation(value = "用户列表", notes = "用户列表分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页条目", dataType = "Integer")
    })
    @LogOutput
    @GetMapping("info/list")
    public ApiResult<IPage<UserSimpleResp>> list(@RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @NotNull(message = "页码错误")
                                                 @Min(value = 1L, message = "页码错误")
                                                 @Max(value = Integer.MAX_VALUE, message = "页码错误")
                                                 @RequestParam(value = "page") Integer page,
                                                 @NotNull(message = "分页条数错误")
                                                 @Max(value = 30L, message = "分页条数错误")
                                                 @Min(value = 5L, message = "分页条数错误")
                                                 @RequestParam(value = "size") Integer size) {
        // 手机号码格式校验
        if (StringUtils.isNotBlank(phone) && NumberUtil.isNumber(phone) && phone.length() > CommConstant.FOUR) {
            throw new CustomException(UserErrorCode.PHONE_FORMAT_ERR);
        }
        // 用户名格式校验
        if (StringUtils.isNotBlank(name) && !java.util.regex.Pattern.compile(USERNAME_REGEX).matcher(name).matches()) {
            throw new CustomException(UserErrorCode.NAME_FORMAT_ERR);
        }
        return ApiResult.ok(userBusiness.getUserInfoPage(name, phone, page, size));
    }

    @ApiOperation(value = "用户详情", notes = "根据ID获取用户详情信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @LogOutput
    @GetMapping("info/detail")
    public ApiResult<UserDetailVo> detail(@Pattern(regexp = "\\d{14,31}$", message = "用户ID格式错误")
                                          @RequestParam("id") String id) {
        return ApiResult.ok(userBusiness.getUserInfoDetail(id));
    }

    @ApiOperation(value = "用户删除", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @LogOutput
    @DeleteMapping("info/del")
    public ApiResult<Boolean> del(@NotBlank(message = "用户ID不能为空")
                                  @Pattern(regexp = "\\d{14,31}$", message = "用户ID格式错误")
                                  @RequestParam("id") String id) {
        return ApiResult.ok(userBusiness.logOffUserInfo(id, getUserId()));
    }

}
