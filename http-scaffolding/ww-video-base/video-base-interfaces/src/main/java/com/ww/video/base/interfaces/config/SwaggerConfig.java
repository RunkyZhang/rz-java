package com.ww.video.base.interfaces.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// http://localhost:9090/swagger-ui.html
@Configuration
public class SwaggerConfig {
    @Value("${swagger.enable:false}")
    private boolean enable;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enable)
                .apiInfo(new ApiInfoBuilder()
                        .title("视频服务")
                        .description("视频功能接口的文档")
                        .version("90.90")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ww.video.base.interfaces.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
