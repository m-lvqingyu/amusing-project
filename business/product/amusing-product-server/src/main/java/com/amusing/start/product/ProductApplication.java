package com.amusing.start.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Lv.QingYu
 * @description: 商品库存服务
 * @since 2021/10/16
 */
@EnableSwagger2
@EnableOpenApi
@SpringBootApplication
@EnableFeignClients(basePackages = "com.amusing.start.client")
@EnableTransactionManagement
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
    
}
