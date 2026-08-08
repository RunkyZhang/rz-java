package com.rz.langchain.demo.server.harnees;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface NotifyAgent {
    @Agent(name = "notify_specialist", description = "通信专家：能查找通信人，能通过各种渠道发送消息，目前只支持企业微信（WcIm）")
    @UserMessage("执行这个文件任务：{{task}}")
    String work(@V("task") String task);
}