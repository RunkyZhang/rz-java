package com.rz.web.demo.server.tool;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;

public interface McpTool {
    default McpServerFeatures.SyncToolSpecification buildTool() {
        return new McpServerFeatures.SyncToolSpecification(new McpSchema.Tool(getName(), getDescription(), getInputSchema()), this::callback);
    }

    String getName();

    // 工具描述，可使用Prompt
    String getDescription();

    // 入参数模版
    String getInputSchema();

    McpSchema.CallToolResult callback(McpSyncServerExchange exchange, Map<String, Object> arguments);
}
