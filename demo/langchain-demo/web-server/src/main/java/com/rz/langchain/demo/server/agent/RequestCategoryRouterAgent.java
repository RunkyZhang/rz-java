package com.rz.langchain.demo.server.agent;

import com.rz.langchain.demo.server.dto.RequestCategoryEnum;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;
import dev.langchain4j.service.UserMessage;

public interface RequestCategoryRouterAgent {
    @UserMessage("""
        分析用户的请求「{{request}}」，将其分类为 'LEGAL'（法律）、'MEDICAL'（医学）或 'TECHNICAL'（技术）。
        如果请求不属于以上任何类别，则分类为 'UNKNOWN'（未知）。
        仅回复其中一个单词，不要包含任何其他内容。
        """)
    @Agent("对用户请求进行分类的路由器")
    RequestCategoryEnum classify(@V("request") String request);
}
