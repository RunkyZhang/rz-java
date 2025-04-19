package com.rz.web.demo.server.tool;

import com.rz.web.demo.server.schema.EnumArgument;
import com.rz.web.demo.server.schema.InputSchema;
import com.rz.web.demo.server.schema.StringArgument;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class CalcMcpTool implements McpTool {
    @Override
    public String getName() {
        return "calc";
    }

    @Override
    public String getDescription() {
        return "简介：runky的另一个基础计算；" +
                "功能：根据传入不同的操作类型，对a和b做不同的计算；" +
                "参数说明：operation类型包括zhenxiang，wang2mazi；" +
                "参数说明：a和b都是int类型。";
    }

    @Override
    public String getInputSchema() {
        return new InputSchema()
                .withId("zzzyyyzzz")
                .withEnum("operation", true, Arrays.asList("houhou", "wang2mazi", "zhenxiang"))
                .withNumber("a", true)
                .withNumber("b", true)
                .build();
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
        } else if ("zhenxiang".equals(type)) {
            result = a - b - 1;
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
