package com.amusing.start.product.controller.inward;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import com.amusing.start.product.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@RestController
public class ProductInwardController implements ProductClient {

    private final IProductService productService;

    @Autowired
    public ProductInwardController(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ShopCarOutput> shopCar(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(ErrorCode.PARAMETER_ERR);
        }
        return productService.shopCar(userId);
    }

    @Override
    public Boolean deductionStock(List<StockDeductionInput> inputs) throws CustomException {
        return productService.deductionStock(inputs);
    }


}
