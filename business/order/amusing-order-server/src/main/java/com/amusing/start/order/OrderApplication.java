package com.amusing.start.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Create By 2021/8/21
 *
 * @author lvqingyu
 */
@EnableSwagger2
@EnableOpenApi
@SpringBootApplication(scanBasePackages = {"com.amusing.start"})
@EnableFeignClients(basePackages = "com.amusing.start.client")
@MapperScan("com.amusing.start.order.mapper")
@EnableTransactionManagement
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
