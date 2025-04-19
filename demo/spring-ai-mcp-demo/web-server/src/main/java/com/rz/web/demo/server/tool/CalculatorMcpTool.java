package com.rz.web.demo.server.tool;

import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CalculatorMcpTool implements McpTool {
    @Override
    public String getName() {
        return "calculator";
    }

    @Override
    public String getDescription() {
        return "简介：runky的基础计算；" +
                "功能：根据传入不同的操作类型，对a和b做不同的计算；" +
                "参数限制1：操作类型包括houhou，wang2mazi；" +
                "参数限制2：a和b都是int类型。";
    }

    @Override
    public String getSchema() {
        return "{\n" +
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
    }

    @Override
    public McpSchema.CallToolResult callback(McpSyncServerExchange exchange, Map<String, Object> arguments) {
        log.info("arguments: {}", arguments);

        String type = (String) arguments.get("operation");
        int a = (int) arguments.get("a");
        int b = (int) arguments.get("b");

        int result = -1;
        if ("houhou".equals(type)) {
            result = a + b + 100000;
        } else if ("wang2mazi".equals(type)) {
            result = a * b + 100000;
        }

        // 工具实现
        McpSchema.TextContent textContent = new McpSchema.TextContent(
                Collections.singletonList(McpSchema.Role.USER),
                1.0,
                "The result is " + result
        );
        return new McpSchema.CallToolResult(Collections.singletonList(textContent), false);
    }
}
