package com.rz.langchain.demo.server.tools;

import com.rz.langchain.demo.server.Helper;
import com.rz.langchain.demo.server.JacksonHelper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.service.tool.ToolExecutionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
public class WebTools {
    @Value("${ai.model.bailian.apiKey:}")
    private String apiKey;

    private String getApiKey() {
        if (StringUtils.isBlank(apiKey)) {
            apiKey = Helper.loadProperties(false).get("ai.model.bailian.apiKey");
        }
        return apiKey;
    }

    @Tool("通过阿里云百炼 MCP 进行网页搜索")
    public String search(@P("搜索关键词") String query) throws Exception {
        McpTransport transport = StreamableHttpMcpTransport.builder()
                .url("https://dashscope.aliyuncs.com/api/v1/mcps/WebSearch/mcp")
                .customHeaders(Map.of("Authorization", "Bearer " + getApiKey()))
                .timeout(Duration.ofSeconds(30))
                .build();

        try (McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("WebSearch")
                .transport(transport)
                .build()) {

            ToolSpecification toolSpecification = mcpClient.listTools().stream()
                    .filter(o -> {
                        if (null == o || StringUtils.isBlank(o.name())) {
                            return false;
                        }

                        return o.name().toLowerCase().contains("search");
                    })
                    .findFirst()
                    .orElse(null);
            if (null == toolSpecification) {
                return "cannot to search web; the mcp is not a web search tool";
            }
            log.info("tool name: {}", toolSpecification.name());

            String arguments = JacksonHelper.toJson(Map.of("query", query), false);
            ToolExecutionRequest request = ToolExecutionRequest.builder()
                    .name(toolSpecification.name())
                    .arguments(arguments)
                    .build();

            ToolExecutionResult toolExecutionResult = mcpClient.executeTool(request, null);
            if (null == toolExecutionResult || toolExecutionResult.isError()) {
                return "failed to search query on web: " + query;
            } else {
                return toolExecutionResult.toString();
            }
        }
    }

    @Tool("通过阿里云百炼 MCP 获取网页内容")
    @SuppressWarnings("removal")
    public String parser(@P("要获取的 URL") String url) throws Exception {
        // HttpMcpTransport被Deprecated弃用了。但是这个mcp就是用sse协议，所以只能使用HttpMcpTransport
        McpTransport transport = dev.langchain4j.mcp.client.transport.http.HttpMcpTransport.builder()
                .sseUrl("https://dashscope.aliyuncs.com/api/v1/mcps/WebParser/sse")
                .customHeaders(Map.of("Authorization", "Bearer " + getApiKey()))
                .timeout(Duration.ofSeconds(30))
                .build();

        try (McpClient mcpClient = new DefaultMcpClient.Builder()
                .key("WebParser")
                .transport(transport)
                .build()) {

            ToolSpecification toolSpecification = mcpClient.listTools().stream()
                    .filter(o -> {
                        if (null == o || StringUtils.isBlank(o.name())) {
                            return false;
                        }

                        return o.name().toLowerCase().contains("fetch") || o.name().toLowerCase().contains("parser");
                    })
                    .findFirst()
                    .orElse(null);
            if (null == toolSpecification) {
                return "cannot to parser(fetch) web; the mcp is not a web parser(fetch) tool";
            }
            log.info("tool name: {}", toolSpecification.name());

            String arguments = JacksonHelper.toJson(Map.of("url", url), false);
            ToolExecutionRequest request = ToolExecutionRequest.builder()
                    .name(toolSpecification.name())
                    .arguments(arguments)
                    .build();

            ToolExecutionResult toolExecutionResult = mcpClient.executeTool(request, null);
            if (null == toolExecutionResult || toolExecutionResult.isError()) {
                return "failed to parser url: " + url;
            } else {
                return toolExecutionResult.toString();
            }
        }
    }
}
