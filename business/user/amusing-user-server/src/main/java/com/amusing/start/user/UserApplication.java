package com.amusing.start.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Lv.QingYu
 * @description: 用户服务
 * @since 2021/09/21
 */
@EnableOpenApi
@SpringBootApplication
@ComponentScan(value = {"com.amusing.start"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }


}
