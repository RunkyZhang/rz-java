package com.rz.langchain.demo.server.harnees;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface NotifyAgent {
    @Agent(name = "notify_specialist", description = "通信专家：能查找通信人，能通过各种渠道发送消息，目前只支持企业微信（WcIm）")
    @SystemMessage("你是通信专家。职责是调用工具查找联系人并发送企业微信消息。直接调用工具完成任务，禁止返回JSON格式的agent调用决策。")
    @UserMessage("执行这个通信任务：{{task}}")
    String work(@V("task") String task);
}