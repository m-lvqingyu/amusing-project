package com.amusing.start.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Lv.QingYu
 * @description: Swagger文档配置（http://ip:port/swagger-ui/index.html）
 * @since 2023/03/06
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title("Amusing-User在线接口文档")
                        .version("v1.0")
                        .description("Amusing项目接口文档")
                        .contact(new Contact("lv.QingYu", "https://juejin.cn", "759058217@qq.com"))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.amusing.start.user.controller.outward"))
                .build();
    }


}
