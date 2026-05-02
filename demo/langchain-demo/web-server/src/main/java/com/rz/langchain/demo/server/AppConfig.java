package com.rz.langchain.demo.server;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${ai.model.apiKey}")
    public String apiKey;

    // http debug：SyncRequestExecutor
    @Bean("qwen_3_5_plus")
    public OpenAiChatModel openAiChatModel_Qwen() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("qwen3.5-plus")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("glm_5")
    public OpenAiChatModel openAiChatModel_Qlm() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("glm-5")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(apiKey)
                .modelName("qwen3.5-plus")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatMemory ChatMemory() {
        // memoryId: 一般被设计为sessionId
        return MessageWindowChatMemory.builder()
                .id(1111)
                .maxMessages(50)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }

    // strictJsonSchema
}
