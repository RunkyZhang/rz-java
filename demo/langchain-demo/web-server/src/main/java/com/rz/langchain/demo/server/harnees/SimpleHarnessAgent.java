package com.rz.langchain.demo.server.harnees;


import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.V;

public interface SimpleHarnessAgent extends AgenticScopeAccess {
//    @Agent(name = "supervisor")
//    String chat(@V("request") String request);

    @Agent(name = "supervisor")
    ResultWithAgenticScope<String> chat(@MemoryId String memoryId, @V("request") String request);
}