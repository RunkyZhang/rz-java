package com.rz.web.demo.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class AppConfig {
    private static final String MESSAGE_ENDPOINT = "/mcp/message";

    @Bean
    public WebMvcSseServerTransportProvider webMvcSseServerTransportProvider() {
        WebMvcSseServerTransportProvider webMvcSseServerTransportProvider = new WebMvcSseServerTransportProvider(new ObjectMapper(), MESSAGE_ENDPOINT);

        McpServer.async(webMvcSseServerTransportProvider)
                .serverInfo("my-server", "1.0.0")
                .build();

        return webMvcSseServerTransportProvider;
    }

    // 把SSE服务endpoint注入mvc路由
    @Bean
    public RouterFunction<ServerResponse> routerFunction(WebMvcSseServerTransportProvider webMvcSseServerTransportProvider) {
        return webMvcSseServerTransportProvider.getRouterFunction();
    }
}
