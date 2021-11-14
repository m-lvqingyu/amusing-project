package com.amusing.start.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create By 2021/10/16
 *
 * @author lvqingyu
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.amusing.start.client")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
