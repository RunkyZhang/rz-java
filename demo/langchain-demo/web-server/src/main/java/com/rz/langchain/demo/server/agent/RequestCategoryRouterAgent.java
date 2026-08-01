package com.rz.langchain.demo.server.agent;

import com.rz.langchain.demo.server.dto.RequestCategoryEnum;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.V;
import dev.langchain4j.service.UserMessage;

public interface RequestCategoryRouterAgent {
    @UserMessage("""
        分析用户的请求「{{request}}」，将其分类为 'LEGAL'（法律）、'MEDICAL'（医学）或 'TECHNICAL'（技术）。
        如果请求不属于以上任何类别，则分类为 'UNKNOWN'（未知）。
        仅回复其中一个单词，不要包含任何其他内容。
        """)
    // 如果不设置name，则使用类名作为agent name
    // 看起来只有用@MemoryId。才能获取固定的memoryId。否则每次对话会重新创建一个。涉及的agent都要用这种方式才能从上到下获取memoryId
    @Agent("对用户请求进行分类的路由器")
    RequestCategoryEnum classify(@MemoryId String memoryId, @V("request") String request);
}
