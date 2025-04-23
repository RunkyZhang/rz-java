package com.rz.web.demo.server.tool;

import io.modelcontextprotocol.server.McpAsyncServerExchange;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface McpTool {
    default McpServerFeatures.SyncToolSpecification buildSyncTool() {
        return new McpServerFeatures.SyncToolSpecification(new McpSchema.Tool(getName(), getDescription(), getInputSchema()), this::syncCallback);
    }

    default McpServerFeatures.AsyncToolSpecification buildAsyncTool() {
        return new McpServerFeatures.AsyncToolSpecification(new McpSchema.Tool(getName(), getDescription(), getInputSchema()), this::asyncCallback);
    }

    String getName();

    // 工具描述，可使用Prompt
    String getDescription();

    // 入参数模版
    String getInputSchema();

    McpSchema.CallToolResult syncCallback(McpSyncServerExchange exchange, Map<String, Object> arguments);

    Mono<McpSchema.CallToolResult> asyncCallback(McpAsyncServerExchange exchange, Map<String, Object> arguments);
}
