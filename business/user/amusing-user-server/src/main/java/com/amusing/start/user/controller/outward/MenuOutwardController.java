package com.amusing.start.user.controller.outward;

import com.amusing.start.controller.BaseController;
import com.amusing.start.exception.CustomException;
import com.amusing.start.result.ApiResult;
import com.amusing.start.user.entity.vo.MenuVo;
import com.amusing.start.user.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 2023/02/24.
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("user/outward/menu")
public class MenuOutwardController extends BaseController {

    private final IMenuService menuService;

    @Autowired
    public MenuOutwardController(HttpServletRequest request, IMenuService menuService) {
        super(request);
        this.menuService = menuService;
    }

    @GetMapping("tree")
    public ApiResult<List<MenuVo>> tree() throws CustomException {
        return ApiResult.ok(menuService.getMenuTree(getUserId()));
    }

}
