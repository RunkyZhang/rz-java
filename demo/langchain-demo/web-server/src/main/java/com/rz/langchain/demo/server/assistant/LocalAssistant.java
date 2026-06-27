package com.rz.langchain.demo.server.assistant;

import dev.langchain4j.service.UserMessage;

public interface LocalAssistant {
    @UserMessage("""
            判断下面这句话是用户是在打招呼吗？
            ====
            {{it}}
            """)
    boolean isSayHello(String userMessage);
}
