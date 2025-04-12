package com.rz.web.demo.client;

import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public McpClientTransport mcpClientTransport() {
        return new HttpClientSseClientTransport("http://localhost:8080");
    }

    @Bean
    public McpSyncClient mcpSyncClient(McpClientTransport mcpClientTransport) {
        // Create a sync client with custom configuration
        McpSyncClient mcpSyncClient = McpClient.sync(mcpClientTransport)
                .requestTimeout(Duration.ofSeconds(10))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)      // Enable roots capability
                        .sampling()       // Enable sampling capability
                        .build())
                .sampling(request -> {
                    McpSchema.CreateMessageResult createMessageResult = new McpSchema.CreateMessageResult(
                            McpSchema.Role.USER,
                            new McpSchema.TextContent(List.of(McpSchema.Role.ASSISTANT), 1.0, "Hello, world!"),
                            "gpt-3.5-turbo",
                            McpSchema.CreateMessageResult.StopReason.END_TURN
                    );

                    return createMessageResult;
                })
                .toolsChangeConsumer(tools -> Mono.fromRunnable(() -> {
                    System.out.println("Tools updated: " + tools);
                }))
                .resourcesChangeConsumer(resources -> Mono.fromRunnable(() -> {
                    System.out.println("Resources updated: " + resources);
                }))
                .promptsChangeConsumer(prompts -> Mono.fromRunnable(() -> {
                    System.out.println("Prompts updated: " + prompts);
                }))
                .build();

//        mcpSyncClient.setLoggingLevel(McpSchema.LoggingLevel.INFO);

        // Initialize connection
        mcpSyncClient.initialize();

        return mcpSyncClient;
    }

    @Bean
    public McpAsyncClient mcpAsyncClient() {
        McpClientTransport mcpClientTransport = new HttpClientSseClientTransport("http://localhost:8080");

        // Create a sync client with custom configuration
        McpAsyncClient mcpAsyncClient = McpClient.async(mcpClientTransport)
                .requestTimeout(Duration.ofSeconds(10))
                .capabilities(McpSchema.ClientCapabilities.builder()
                        .roots(true)      // Enable roots capability
                        .sampling()       // Enable sampling capability
                        .build())
                .sampling(request -> {
                    McpSchema.CreateMessageResult createMessageResult = new McpSchema.CreateMessageResult(
                            McpSchema.Role.USER,
                            new McpSchema.TextContent(List.of(McpSchema.Role.ASSISTANT), 1.0, "Hello, world!"),
                            "gpt-3.5-turbo",
                            McpSchema.CreateMessageResult.StopReason.END_TURN
                    );
                    return Mono.just(createMessageResult);
                })
                .toolsChangeConsumer(tools -> Mono.fromRunnable(() -> {
                    System.out.println("Tools updated: " + tools);
                }))
                .resourcesChangeConsumer(resources -> Mono.fromRunnable(() -> {
                    System.out.println("Resources updated: " + resources);
                }))
                .promptsChangeConsumer(prompts -> Mono.fromRunnable(() -> {
                    System.out.println("Prompts updated: " + prompts);
                }))
                .build();

        mcpAsyncClient.setLoggingLevel(McpSchema.LoggingLevel.INFO);

        // Initialize connection
        mcpAsyncClient.initialize();

        return mcpAsyncClient;
    }
}
