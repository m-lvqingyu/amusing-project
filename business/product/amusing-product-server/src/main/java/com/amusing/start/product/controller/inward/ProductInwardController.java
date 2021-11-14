package com.amusing.start.product.controller.inward;

import com.amusing.start.client.api.ProductClient;
import com.amusing.start.client.output.ProductOutput;
import com.amusing.start.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create By 2021/10/23
 *
 * @author lvqingyu
 */
@RestController
public class ProductInwardController implements ProductClient {

    private final IProductService productService;

    @Autowired
    public ProductInwardController(IProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductOutput get(String shopId, String productId, String priceId) {
        return productService.getProductDetail(shopId, productId, priceId);
    }

}
