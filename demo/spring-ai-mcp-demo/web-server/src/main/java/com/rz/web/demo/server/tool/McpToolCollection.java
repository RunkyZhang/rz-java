package com.rz.web.demo.server.tool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class McpToolCollection {
    private final Map<String, McpTool> mcpTools = new HashMap<>();

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    private void init() {
        Map<String, McpTool> eventDomainBeans = applicationContext.getBeansOfType(McpTool.class);
        for (McpTool mcpTool : eventDomainBeans.values()) {
            if (null == mcpTool) {
                continue;
            }
            this.mcpTools.put(mcpTool.getName(), mcpTool);
        }
    }

    public Collection<McpTool> getAll() {
        return mcpTools.values();
    }
}
