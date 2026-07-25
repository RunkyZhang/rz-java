package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface SimpleRouterAgent {
    @Agent
    String ask(@V("request") String request);
}
