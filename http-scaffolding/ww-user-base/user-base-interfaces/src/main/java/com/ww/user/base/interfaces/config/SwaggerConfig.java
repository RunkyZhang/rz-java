package com.ww.user.base.interfaces.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// http://localhost:7070/swagger-ui.html
@Configuration
public class SwaggerConfig {
    @Value("${swagger.enable:false}")
    private boolean enable;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(new ApiInfoBuilder()
                        .title("用户服务")
                        .description("用户功能接口的文档")
                        .version("70.70")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ww.user.base.interfaces.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
