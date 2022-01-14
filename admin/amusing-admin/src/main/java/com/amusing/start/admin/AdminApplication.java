package com.amusing.start.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.amusing.start")
@EnableFeignClients(basePackages = {"com.amusing.start.client"})
@EnableAspectJAutoProxy
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
