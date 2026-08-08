package com.rz.langchain.demo.server.harnees;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface FileAgent {
    @Agent(name = "file_specialist", description = "文件操作专家：读取、写入、编辑、搜索。")
    @UserMessage("执行这个文件任务：{{task}}")
    String work(@V("task") String task);
}