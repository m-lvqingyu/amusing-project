package com.amusing.start.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关组件(路由 | 熔断 | 限流)
 *
 * @author Lv.QingYu
 * @since 2021/08/21
 */
@SpringBootApplication(scanBasePackages = {"com.amusing.start"})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}