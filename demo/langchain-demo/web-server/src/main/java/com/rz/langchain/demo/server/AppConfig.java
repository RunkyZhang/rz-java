package com.rz.langchain.demo.server;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${ai.model.apiKey}")
    public String apiKey;

    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("qwen3.5-plus")
                .build();
    }
}
