package com.rz.langchain.demo.server.harnees;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface SystemAgent {
    @Agent(name = "system_specialist", description = "Shell命令执行与环境管理专家。")
    @UserMessage("执行这个系统任务：{{task}}")
    String work(@V("task") String task);
}