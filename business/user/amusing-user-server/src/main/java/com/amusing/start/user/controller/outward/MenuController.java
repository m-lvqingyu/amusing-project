package com.amusing.start.user.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.vo.MenuVo;
import com.amusing.start.user.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lv.QingYu
 * @description: 菜单管理
 * @since 2021/09/21
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("user/out/menu")
public class MenuController extends BaseController {

    private final IMenuService menuService;

    @Autowired
    public MenuController(HttpServletRequest request, IMenuService menuService) {
        super(request);
        this.menuService = menuService;
    }

    @ApiOperation(value = "菜单树")
    @GetMapping("tree")
    public ApiResult<List<MenuVo>> tree() {
        return ApiResult.ok(menuService.getMenuTree(getUserId()));
    }

}
