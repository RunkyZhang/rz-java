/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rz.web.demo.client;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {
    @Resource
    private ServerFeignClient serverFeignClient;
    @Resource
    private McpSyncClient mcpSyncClient;

    // http://127.0.0.1:8081/hello?name=lisi
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        String result = serverFeignClient.hello(name);

        return "Client-Hello " + name + ": " + result;
    }

    @GetMapping("/mcp")
    @ResponseBody
    public String mcp(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        McpSchema.Implementation implementation = mcpSyncClient.getServerInfo();
        McpSchema.ServerCapabilities serverCapabilities = mcpSyncClient.getServerCapabilities();
        McpSchema.ListToolsResult listToolsResult = mcpSyncClient.listTools();
        McpSchema.ListPromptsResult listPromptsResult = mcpSyncClient.listPrompts();
        McpSchema.ListResourcesResult listResourcesResult = mcpSyncClient.listResources();
        McpSchema.ListResourceTemplatesResult listResourceTemplatesResult = mcpSyncClient.listResourceTemplates();

        Map<String, Object> arguments = new HashMap<>();

        McpSchema.ReadResourceRequest readResourceRequest = new McpSchema.ReadResourceRequest("custom://resource");
        McpSchema.ReadResourceResult readResourceResult = mcpSyncClient.readResource(readResourceRequest);

        McpSchema.GetPromptRequest getPromptRequest = new McpSchema.GetPromptRequest("greeting", null);
        McpSchema.GetPromptResult getPromptResult = mcpSyncClient.getPrompt(getPromptRequest);

        arguments.put("operation", "wang2mazi");
        arguments.put("a", 333);
        arguments.put("b", 444);
        McpSchema.CallToolRequest callToolRequest = new McpSchema.CallToolRequest("calc", arguments);
        McpSchema.CallToolResult callToolResult = mcpSyncClient.callTool(callToolRequest);

        return "Client-Hello " + name + ": " + implementation + "; " + serverCapabilities + "; " +
                listToolsResult + "; " + listPromptsResult + "; " + listResourcesResult + "; " +
                listResourceTemplatesResult + "; " + readResourceResult + "; " + getPromptResult + "; " +
                callToolResult;
    }

    // http://127.0.0.1:8081/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }
}
