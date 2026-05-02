package com.rz.langchain.demo.server;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.Capability;
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

    @Value("${ai.model.bailian.codeplan.apiKey}")
    public String bailianApiKey;

    @Value("${ai.model.deepseek.apiKey}")
    public String deepseekApiKey;

    // http debug：SyncRequestExecutor
    @Bean("qwen_3_5_plus")
    public OpenAiChatModel openAiChatModel_Qwen_3_5_plus() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
                .modelName("qwen3.5-plus")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("glm_5")
    public OpenAiChatModel openAiChatModel_Qlm_5() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
                .modelName("glm-5")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("deepSeek_v4_pro")
    public OpenAiChatModel openAiChatModel_DeepSeek_v4_pro() {
        return OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-pro")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("deepSeek_v4_flash")
    public OpenAiChatModel openAiChatModel_DeepSeek_v4_flash() {
        return OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-flash")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
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
