package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface TableFormatAgent {
    @UserMessage("""
            把【{{message}}】信息整理成树形结构。
            ======
            注意：有信息系是父子结构
            注意：有些信息的子节点有多个
            注意：输出的树形结构要美观，优雅，用户友好，可读性强
            """)
    @Agent("你是一个把信息作规整，整理成树性结构（美观，优雅，用户友好，可读性强）的Agent。")
    String run(@V("message") String message);
}
