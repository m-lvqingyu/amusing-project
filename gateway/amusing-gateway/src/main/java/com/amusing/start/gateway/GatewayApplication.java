package com.amusing.start.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Lv.QingYu
 * @description: 网关组件(路由 | 熔断 | 限流)
 * @since 2021/08/21
 */
@SpringBootApplication(scanBasePackages = {"com.amusing.start"})
@EnableFeignClients(basePackages = "com.amusing.start.client")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}