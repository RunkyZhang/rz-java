package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface FoodAgent {
    @UserMessage("""
        你是一位专业的晚餐规划助手。
        
        根据当前的心情「{{mood}}」，推荐3道合适的菜。
        输出要求：String List
        - 仅输出菜名列表，每道菜一行
        - 不要序号、不要描述、不要额外说明
        - 格式示例：
            菜名1
            菜名2
            菜名3
        """)
    @Agent("你是一位专业的晚餐规划助手。")
    List<String> findMeal(@V("mood") String mood);
}
