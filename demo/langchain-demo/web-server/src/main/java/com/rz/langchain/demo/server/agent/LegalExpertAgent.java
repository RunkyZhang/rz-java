package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface LegalExpertAgent {
    @UserMessage("""
        你是一位专业的法律专家。
        请从法律角度分析用户的请求「{{request}}」，并提供专业的法律建议。
        回答要求：
        - 引用相关法律法规，说明适用场景
        - 区分民事、刑事、行政等不同法律领域
        - 提示用户注意诉讼时效、管辖权等程序性问题
        - 在回答末尾声明：以上建议仅供参考，具体法律问题请咨询持证律师
        """)
    // 如果不设置name，则使用类名作为agent name
    // 看起来只有用@MemoryId。才能获取固定的memoryId。否则每次对话会重新创建一个。涉及的agent都要用这种方式才能从上到下获取memoryId
    @Agent(name = "legal", value = "你是一位专业的法律专家，精通法律法规，能为用户提供准确的法律分析和建议。")
    String legal(@MemoryId String memoryId, @V("request") String request);
}
