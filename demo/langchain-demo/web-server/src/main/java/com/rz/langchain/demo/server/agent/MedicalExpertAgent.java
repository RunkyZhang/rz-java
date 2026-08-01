package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface MedicalExpertAgent {
    @UserMessage("""
        你是一位专业的医学专家。
        请从医学角度分析用户的请求「{{request}}」，并给出最佳建议。
        回答要求：
        - 使用通俗易懂的语言，避免过多专业术语
        - 如有必要，提供就医建议和注意事项
        - 在回答末尾提示用户：如有严重症状请及时前往医院就诊
        """)
    // 如果不设置name，则使用类名作为agent name
    // 看起来只有用@MemoryId。才能获取固定的memoryId。否则每次对话会重新创建一个。涉及的agent都要用这种方式才能从上到下获取memoryId
    @Agent(name = "medical", value = "你是一位专业的医学专家，擅长从医学角度分析问题并提供专业建议。")
    String medical(@MemoryId String memoryId, @V("request") String request);
}
