package com.amusing.start.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Lv.QingYu
 * @since 2021/09/21
 */
@SpringBootApplication
@ComponentScan(value = {"com.amusing.start"})
@MapperScan("com.amusing.start.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }


}
