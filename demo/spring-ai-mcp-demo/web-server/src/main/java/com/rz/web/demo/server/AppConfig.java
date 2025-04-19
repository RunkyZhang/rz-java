package com.rz.web.demo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rz.web.demo.server.tool.McpTool;
import com.rz.web.demo.server.tool.McpToolCollection;
import io.modelcontextprotocol.server.McpAsyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {
    private static final String MESSAGE_ENDPOINT = "/mcp/message";

    // 同步工具规范
    private static final String schema = "{\n" +
            "              \"type\" : \"object\",\n" +
            "              \"id\" : \"urn:jsonschema:Operation\",\n" +
            "              \"properties\" : {\n" +
            "                \"operation\" : {\n" +
            "                  \"type\" : \"string\"\n" +
            "                },\n" +
            "                \"a\" : {\n" +
            "                  \"type\" : \"number\"\n" +
            "                },\n" +
            "                \"b\" : {\n" +
            "                  \"type\" : \"number\"\n" +
            "                }\n" +
            "              }\n" +
            "            }";

    @Resource
    private McpToolCollection mcpToolCollection;

    @Bean
    public WebMvcSseServerTransportProvider webMvcSseServerTransportProvider() {
        return new WebMvcSseServerTransportProvider(new ObjectMapper(), MESSAGE_ENDPOINT);
    }

    // 把SSE服务endpoint注入mvc路由
    @Bean
    public RouterFunction<ServerResponse> routerFunction(WebMvcSseServerTransportProvider webMvcSseServerTransportProvider) {
        return webMvcSseServerTransportProvider.getRouterFunction();
    }

    @Bean
    public McpSyncServer mcpSyncServer(WebMvcSseServerTransportProvider webMvcSseServerTransportProvider) {
        McpSyncServer mcpSyncServer = McpServer.sync(webMvcSseServerTransportProvider)
                .serverInfo("demo-sync-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(true, true)     // 启用资源支持
                        .tools(true)         // 启用工具支持
                        .prompts(true)       // 启用提示支持
                        .logging()           // 启用日志支持
                        .build())
                .build();

//        McpServerFeatures.SyncToolSpecification syncToolSpecification = new McpServerFeatures.SyncToolSpecification(
//                new McpSchema.Tool("calculator", "Basic calculator", schema),
//                (exchange, arguments) -> {
//                    // 工具实现
//                    McpSchema.TextContent textContent = new McpSchema.TextContent(
//                            Collections.singletonList(McpSchema.Role.USER),
//                            1.0,
//                            "The textContent is " + arguments
//                    );
//                    return new McpSchema.CallToolResult(Collections.singletonList(textContent), false);
//                }
//        );

        // Sync resource specification
        McpServerFeatures.SyncResourceSpecification syncResourceSpecification = new McpServerFeatures.SyncResourceSpecification(
                new McpSchema.Resource("custom://resource", "name", "description", "mime-type", null),
                (exchange, request) -> {
                    // Resource read implementation
                    McpSchema.ResourceContents resourceContents = new McpSchema.TextResourceContents(
                            "custom://resource",
                            "text/plain",
                            "The resource contents"
                    );
                    return new McpSchema.ReadResourceResult(List.of(resourceContents));
                }
        );

        // Sync prompt specification
        McpServerFeatures.SyncPromptSpecification syncPromptSpecification = new McpServerFeatures.SyncPromptSpecification(
                new McpSchema.Prompt("greeting", "description", List.of(
                        new McpSchema.PromptArgument("name", "description", true)
                )),
                (exchange, request) -> {
                    // Prompt implementation
                    String description = "Hello, {name}!";
                    McpSchema.PromptMessage message = new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT,
                            new McpSchema.TextContent(List.of(McpSchema.Role.ASSISTANT), 1.0, description));
                    return new McpSchema.GetPromptResult(description, List.of(message));
                }
        );

        // 注册工具、资源和提示
        Collection<McpTool> mcpTools = this.mcpToolCollection.getAll();
        for (McpTool mcpTool : mcpTools) {
            mcpSyncServer.addTool(mcpTool.buildTool());
        }
        mcpSyncServer.addResource(syncResourceSpecification);
        mcpSyncServer.addPrompt(syncPromptSpecification);

        // 发送日志通知
        mcpSyncServer.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.DEBUG)
                .logger("custom-logger")
                .data("Server initialized")
                .build());

        return mcpSyncServer;
    }

    private void buildAsyncServer(WebMvcSseServerTransportProvider webMvcSseServerTransportProvider) {
        McpAsyncServer mcpAsyncServer = McpServer.async(webMvcSseServerTransportProvider)
                .serverInfo("demo-async-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(true, true)     // 启用资源支持
                        .tools(true)         // 启用工具支持
                        .prompts(true)       // 启用提示支持
                        .logging()           // 启用日志支持
                        .build())
                .build();

        McpServerFeatures.AsyncToolSpecification asyncToolSpecification = new McpServerFeatures.AsyncToolSpecification(
                new McpSchema.Tool("calculator", "Basic calculator", schema),
                (exchange, arguments) -> {
                    // Tool implementation
                    McpSchema.TextContent textContent = new McpSchema.TextContent(
                            Collections.singletonList(McpSchema.Role.USER),
                            1.0,
                            "The textContent is " + arguments
                    );
                    return Mono.just(new McpSchema.CallToolResult(Collections.singletonList(textContent), false));
                }
        );

        // Async resource specification
        McpServerFeatures.AsyncResourceSpecification asyncResourceSpecification = new McpServerFeatures.AsyncResourceSpecification(
                new McpSchema.Resource("custom://resource", "name", "description", "mime-type", null),
                (exchange, request) -> {
                    // Resource read implementation
                    McpSchema.ResourceContents resourceContents = new McpSchema.TextResourceContents(
                            "custom://resource",
                            "text/plain",
                            "The resource contents"
                    );
                    return Mono.just(new McpSchema.ReadResourceResult(List.of(resourceContents)));
                }
        );

        // Async prompt specification
        McpServerFeatures.AsyncPromptSpecification asyncPromptSpecification = new McpServerFeatures.AsyncPromptSpecification(
                new McpSchema.Prompt("greeting", "description", List.of(
                        new McpSchema.PromptArgument("name", "description", true)
                )),
                (exchange, request) -> {
                    // Prompt implementation
                    String description = "Hello, {name}!";
                    McpSchema.PromptMessage message = new McpSchema.PromptMessage(
                            McpSchema.Role.ASSISTANT,
                            new McpSchema.TextContent(List.of(McpSchema.Role.ASSISTANT), 1.0, description));
                    return Mono.just(new McpSchema.GetPromptResult(description, List.of(message)));
                }
        );

        // 注册工具、资源和提示
        mcpAsyncServer.addTool(asyncToolSpecification);
        mcpAsyncServer.addResource(asyncResourceSpecification);
        mcpAsyncServer.addPrompt(asyncPromptSpecification);

        // 发送日志通知给client
        mcpAsyncServer.loggingNotification(McpSchema.LoggingMessageNotification.builder()
                .level(McpSchema.LoggingLevel.INFO)
                .logger("custom-logger")
                .data("Custom log message")
                .build());
    }
}
