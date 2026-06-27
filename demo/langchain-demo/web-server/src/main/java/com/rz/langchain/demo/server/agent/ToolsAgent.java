package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ToolsAgent {
    @UserMessage("""
            判断【{{message}}】是否需要调用本地工具
            如果需要：则调用，并把调用什么工具，调用参数，调用结果整理返回
            如果不需要：则明确表明无需调用工具，并把【{{message}}】返回
            """)
    @Agent("你是一个专业调用本地工具的Agent。你会根据你所拥有的本地工具能力，来判断消息内容是否需要调用工具。如果需要则调用")
    String run(@V("message") String message);
}
