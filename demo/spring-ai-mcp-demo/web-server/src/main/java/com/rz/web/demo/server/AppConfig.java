package com.rz.web.demo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rz.web.demo.server.tool.McpTool;
import com.rz.web.demo.server.tool.McpToolCollection;
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

import java.util.Collection;
import java.util.List;

@Configuration
public class AppConfig {
    private static final String MESSAGE_ENDPOINT = "/mcp/message";

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
}
