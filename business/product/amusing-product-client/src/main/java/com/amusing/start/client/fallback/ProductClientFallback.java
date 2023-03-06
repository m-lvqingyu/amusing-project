package com.amusing.start.client.fallback;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.input.StockDeductionInput;
import com.amusing.start.client.output.ShopCarOutput;
import com.amusing.start.code.ErrorCode;
import com.amusing.start.exception.CustomException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@Slf4j
@Component
public class ProductClientFallback implements FallbackFactory<ProductClient> {

    @Override
    public ProductClient create(Throwable throwable) {

        return new ProductClient() {
            @Override
            public List<ShopCarOutput> shopCar(String userId) throws CustomException {
                throw new CustomException(ErrorCode.DEGRADE_ERR);
            }

            @Override
            public Boolean deductionStock(List<StockDeductionInput> inputs) throws CustomException {
                throw new CustomException(ErrorCode.DEGRADE_ERR);
            }
        };
    }

}
