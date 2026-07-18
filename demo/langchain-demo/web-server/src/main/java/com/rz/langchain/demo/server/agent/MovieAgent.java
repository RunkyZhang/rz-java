package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface MovieAgent {
    @UserMessage("""
        你是一位专业的电影推荐助手。

        根据当前的心情「{mood}」，推荐3部合适的电影。
        输出要求：String List
        - 每行一部电影名称
        - 不要包含其他内容
        - 格式示例：
            电影1
            电影2
            电影3
        """)
    @Agent("你是一位专业的电影推荐助手。")
    List<String> findMovie(@V("mood") String mood);
}
