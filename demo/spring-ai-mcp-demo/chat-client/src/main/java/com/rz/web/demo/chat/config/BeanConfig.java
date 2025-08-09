package com.rz.web.demo.chat.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        ChatClient dashScopeChatClient = chatClientBuilder
                .defaultSystem("你是一个博学的智能聊天助手，请根据用户提问回答！")
                // TODO
                // 实现 Chat Memory 的 Advisor
                // 在使用 Chat Memory 时，需要指定对话 ID，以便 Spring AI 处理上下文。
//				 .defaultAdvisors(
//						 new MessageChatMemoryAdvisor(new InMemoryChatMemory())
//				 )
                // 实现 Logger 的 Advisor
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                // 设置 ChatClient 中 ChatModel 的 Options 参数
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .withEnableSearch(true)
                                .withEnableThinking(true)
                                .build()
                )
                .build();

        return dashScopeChatClient;
    }
}
