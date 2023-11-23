package com.amusing.start.product.controller.inward;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.CommCode;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.business.ProductBusiness;
import com.amusing.start.product.service.IProductService;
import com.amusing.start.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Lv.QingYu
 * @description: 商品库存服务内部接口
 * @since 2021/10/31
 */
@Slf4j
@RestController
public class ProductInwardController implements ProductClient {

    private final ProductBusiness productBusiness;

    @Autowired
    public ProductInwardController(ProductBusiness productBusiness) {
        this.productBusiness = productBusiness;
    }

    @Override
    public List<ShopCarOutput> shopCar(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommCode.PARAMETER_ERR);
        }
        return productBusiness.shopCar(userId);
    }

    @Override
    public ApiResult<Boolean> deductionStock(List<StockDeductionInput> inputs) throws CustomException {
        return ApiResult.ok(productBusiness.deductionStock(inputs));
    }


}
