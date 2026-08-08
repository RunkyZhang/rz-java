package com.rz.langchain.demo.server.harnees;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface WebAgent {
    @Agent(name = "web_specialist", description = "网页搜索与URL内容获取专家。")
    @UserMessage("执行这个网页任务：{{task}}")
    String work(@V("task") String task);
}