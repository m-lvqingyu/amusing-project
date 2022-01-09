package com.amusing.start.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Create By 2021/8/28
 *
 * @author lvqingyu
 */
@SpringBootApplication
@ComponentScan(value = {"com.amusing.start"})
@EnableFeignClients(basePackages = {"com.amusing.start.client"})
@EnableAspectJAutoProxy
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
