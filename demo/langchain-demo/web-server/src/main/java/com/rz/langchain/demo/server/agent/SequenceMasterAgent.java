package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.service.V;

public interface SequenceMasterAgent {
    @Agent("你是多个ai agent中的master。负责调用，协调，编排其他ai agent。")
    String chat(@V("message") String message);
}
