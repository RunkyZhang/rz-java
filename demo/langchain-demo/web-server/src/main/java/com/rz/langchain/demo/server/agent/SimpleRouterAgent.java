package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.V;

public interface SimpleRouterAgent {
    // 如果不设置name，则使用类名作为agent name
    // 看起来只有用@MemoryId。才能获取固定的memoryId。否则每次对话会重新创建一个。涉及的agent都要用这种方式才能从上到下获取memoryId
    @Agent
    String ask(@MemoryId String memoryId, @V("request") String request);
}
