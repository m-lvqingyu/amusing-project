package com.amusing.start.search.controller.inward;

import com.amusing.start.client.api.ShopSearchClient;
import com.amusing.start.client.input.ShopChangeInput;
import com.amusing.start.client.input.ShopPageInput;
import com.amusing.start.client.output.ShopOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.result.ApiResult;
import com.amusing.start.search.exception.SearchException;
import com.amusing.start.search.service.IShopService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lv.qingyu
 */
@Slf4j
@RestController
public class ShopInwardController implements ShopSearchClient {

    private final IShopService shopService;

    @Autowired
    public ShopInwardController(IShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public ApiResult<Boolean> change(ShopChangeInput input) {
        try {
            boolean result = shopService.change(input);
            return ApiResult.ok(result);
        } catch (SearchException e) {
            log.error("[search]-shop change err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            return ApiResult.result(e.getResultCode());
        }
    }

    @Override
    public ApiResult<List<ShopOutput>> shopPage(ShopPageInput input) {
        if (input == null) {
            log.warn("[search]-shopPage request param is null");
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        try {
            List<ShopOutput> shopOutputList = shopService.shopPage(input);
            return ApiResult.ok(shopOutputList);
        } catch (SearchException e) {
            log.error("[search]-shopPage err! param:{}, msg:{}", input, Throwables.getStackTraceAsString(e));
            return ApiResult.result(e.getResultCode());
        }
    }

    @Override
    public ApiResult<ShopOutput> getDetail(String id) {
        if (StringUtils.isEmpty(id)) {
            log.warn("[search]-shop getDetail param is null");
            return ApiResult.result(CommCode.PARAMETER_EXCEPTION);
        }
        try {
            ShopOutput detail = shopService.getDetail(id);
            return ApiResult.ok(detail);
        } catch (SearchException e) {
            log.error("[search]-shop getDetail err! param:{}, msg:{}", id, Throwables.getStackTraceAsString(e));
            return ApiResult.result(e.getResultCode());
        }
    }

}
