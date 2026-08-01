package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ContextSummarizer {
    @UserMessage("""
            请对以下 AI 智能体与用户之间的对话进行简要总结，最多两句话。
            用户对话内容为：'{{conversation}}'。
            """)
    @Agent
    String summarize(@V("conversation") String conversation);
}
