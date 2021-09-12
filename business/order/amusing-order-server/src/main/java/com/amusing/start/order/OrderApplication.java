package com.amusing.start.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.amusing.start.client.api")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
