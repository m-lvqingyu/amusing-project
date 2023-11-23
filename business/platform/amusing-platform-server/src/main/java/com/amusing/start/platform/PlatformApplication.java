package com.amusing.start.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Lv.QingYu
 * @description: 三方服务API接口
 * @since 2023/10/3
 */
@SpringBootApplication(scanBasePackages = {"com.amusing.start"})
@EnableFeignClients(basePackages = "com.amusing.start.client")
@MapperScan("com.amusing.start.platform.mapper")
public class PlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }

}
