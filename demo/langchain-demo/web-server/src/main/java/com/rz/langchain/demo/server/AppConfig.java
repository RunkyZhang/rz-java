package com.rz.langchain.demo.server;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${ai.model.apiKey}")
    public String apiKey;

    // http debug：SyncRequestExecutor
    @Bean
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("qwen3.5-plus")
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("qwen3.5-plus")
                .build();
    }
}
