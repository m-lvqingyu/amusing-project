package com.amusing.start.user.controller.outward;

import com.amusing.start.code.ErrorCode;
import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.log.RequestUtils;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.constant.CacheKey;
import com.amusing.start.user.entity.dto.LoginDto;
import com.amusing.start.user.entity.dto.RegisterDto;
import com.amusing.start.user.entity.vo.TokenVo;
import com.amusing.start.user.enums.RegisterPreType;
import com.amusing.start.user.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward")
public class UserOutwardController extends BaseController {

    private final IUserService userService;
    private final RedissonClient redissonClient;

    @Autowired
    UserOutwardController(HttpServletRequest request,
                          IUserService userService,
                          RedissonClient redissonClient) {
        super(request);
        this.userService = userService;
        this.redissonClient = redissonClient;
    }

    private static final String CLASS_NAME = "UserOutwardController";
    private static final String METHOD = "registerPre";
    private static final Long TIME_TO_LIVE = 5L;
    private static final long REQUEST_MAX_LIMIT = 10L;

    @GetMapping("v1/register/pre/{type}/{param}")
    public ApiResult<Boolean> registerPre(@PathVariable("type") Integer type,
                                          @PathVariable("param") String param,
                                          HttpServletRequest request) throws CustomException {
        if (RegisterPreType.match(type) == null || StringUtils.isBlank(param)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        String ip = RequestUtils.getIp(request);
        if (StringUtils.isBlank(ip)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        String cacheKey = CacheKey.requestLimitKey(CLASS_NAME, METHOD, ip);
        RAtomicLong atomicLong = redissonClient.getAtomicLong(cacheKey);
        if (!atomicLong.isExists()) {
            atomicLong.expire(TIME_TO_LIVE, TimeUnit.MILLISECONDS);
        }
        long num = atomicLong.incrementAndGet();
        if (num >= REQUEST_MAX_LIMIT) {
            throw new CustomException(ErrorCode.REQUEST_LIMIT);
        }
        return ApiResult.ok(userService.registerPre(type, param));
    }

    @PostMapping("v1/register")
    public ApiResult<Boolean> register(@Valid @RequestBody RegisterDto dto) throws CustomException {
        return ApiResult.ok(userService.register(dto));
    }

    @PostMapping("v1/login")
    public ApiResult<TokenVo> login(@Valid @RequestBody LoginDto dto) throws CustomException {
        return ApiResult.ok(userService.login(dto));
    }

    @GetMapping("v1/refresh/{token}")
    public ApiResult<TokenVo> refresh(@PathVariable("token") String token) throws CustomException {
        return ApiResult.ok(userService.refresh(token));
    }

}
