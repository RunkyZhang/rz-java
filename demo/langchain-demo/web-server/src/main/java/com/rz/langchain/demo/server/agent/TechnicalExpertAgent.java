package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TechnicalExpertAgent {
    @UserMessage("""
        你是一位专业的技术专家。
        请从技术角度分析用户的请求「{{request}}」，并提供可行的技术方案。
        回答要求：
        - 优先给出具体、可落地的解决方案，而非纯理论分析
        - 涉及技术选型时，说明各方案的优缺点和适用场景
        - 如涉及代码，给出关键示例并附简要说明
        - 注意区分不同技术栈的差异性（如前后端、数据库、运维等）
        """)
    // 如果不设置name，则使用类名作为agent name
    // 看起来只有用@MemoryId。才能获取固定的memoryId。否则每次对话会重新创建一个。涉及的agent都要用这种方式才能从上到下获取memoryId
    @Agent(name = "technical", value = "你是一位专业的技术专家，精通各类技术方案，能为用户提供切实可行的技术建议。")
    String technical(@MemoryId String memoryId, @V("request") String request);
}
