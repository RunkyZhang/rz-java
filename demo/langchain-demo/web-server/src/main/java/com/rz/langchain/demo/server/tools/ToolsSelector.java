package com.rz.langchain.demo.server.tools;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class ToolsSelector {
    // 一个执行者里面有多个工具
    private final Map<String, ToolSpecification> toolSpecifications = new HashMap<>();
    private final Map<String, Object> executors = new HashMap<>();

    @Resource
    private WcImTools wcImTools;
    @Resource
    private WriteArticleTools writeArticleTools;
    @Resource
    private FormatAddressTools formatAddressTools;

    @PostConstruct
    private void init() {
        putTools(ToolSpecifications.toolSpecificationsFrom(wcImTools), wcImTools);
        putTools(ToolSpecifications.toolSpecificationsFrom(writeArticleTools), writeArticleTools);
        putTools(ToolSpecifications.toolSpecificationsFrom(formatAddressTools), formatAddressTools);
    }

    public Object getExecutor(String name) {
        return executors.get(name);
    }

    public Set<Object> getExecutors() {
        // 一个执行者里面有多个工具，所以需要set去重
        return new HashSet<>(executors.values());
    }

    public List<ToolSpecification> getToolSpecifications() {
        return new ArrayList<>(toolSpecifications.values());
    }

    private void putTools(List<ToolSpecification> toolSpecifications, Object executor) {
        if (CollectionUtils.isEmpty(toolSpecifications)) {
            return;
        }

        for (ToolSpecification toolSpecification : toolSpecifications) {
            this.toolSpecifications.put(toolSpecification.name(), toolSpecification);
            this.executors.put(toolSpecification.name(), executor);
        }
    }
}
