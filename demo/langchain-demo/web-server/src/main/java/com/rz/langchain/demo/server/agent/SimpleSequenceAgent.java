package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface SimpleSequenceAgent {
    @Agent("你是多个ai agent中的组织者。负责调用，顺序协调其他ai agent。")
    String chat(@V("message") String message);
}
