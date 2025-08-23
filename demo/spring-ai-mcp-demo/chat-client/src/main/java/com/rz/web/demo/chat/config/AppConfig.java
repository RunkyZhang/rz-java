package com.rz.web.demo.chat.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.client.autoconfigure.NamedClientMcpTransport;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpSseClientProperties;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EnableConfigurationProperties({McpSseClientProperties.class})
@Configuration
public class AppConfig {
    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 300000);
    }

    @Bean
    public List<NamedClientMcpTransport> webFluxClientTransports(McpSseClientProperties sseProperties,
                                                                 ObjectProvider<WebClient.Builder> webClientBuilderProvider,
                                                                 ObjectProvider<ObjectMapper> objectMapperProvider) {
        WebClient.Builder webClientBuilder = webClientBuilderProvider.getIfAvailable(WebClient::builder);
        ObjectMapper objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);

        List<NamedClientMcpTransport> namedClientMcpTransports = new ArrayList<>();
        for (Map.Entry<String, McpSseClientProperties.SseParameters> entry : sseProperties.getConnections().entrySet()) {
            String sseName = entry.getKey();
            McpSseClientProperties.SseParameters sseParameters = entry.getValue();
            WebClient.Builder webClientBuilderClone = webClientBuilder
                    .clone()
                    .baseUrl(sseParameters.url())
                    .defaultHeaders(headers -> headers.add("Content-Type", "application/json"));
            String sseEndpoint = sseParameters.sseEndpoint() != null ? sseParameters.sseEndpoint() : "/sse";
            WebFluxSseClientTransport webFluxSseClientTransport = WebFluxSseClientTransport
                    .builder(webClientBuilderClone)
                    .sseEndpoint(sseEndpoint)
                    .objectMapper(objectMapper)
                    .build();
            namedClientMcpTransports.add(new NamedClientMcpTransport(sseName, webFluxSseClientTransport));
        }

        return namedClientMcpTransports;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
        ChatClient chatClient = chatClientBuilder
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
                .defaultToolCallbacks(toolCallbackProvider)
                .build();

        return chatClient;
    }
}
