package com.amusing.start.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * Create By 2021/9/21
 *
 * @author lvqingyu
 */
@EnableOpenApi
@SpringBootApplication
@ComponentScan(value = {"com.amusing.start"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
