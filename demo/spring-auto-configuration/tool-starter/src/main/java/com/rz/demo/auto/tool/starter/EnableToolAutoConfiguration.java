package com.rz.demo.auto.tool.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ToolProperties.class})
public class EnableToolAutoConfiguration {
    @Autowired
    private ToolProperties toolProperties;

    @Bean
    public CatToolService catToolService() {
        return new CatToolService(toolProperties);
    }
}
