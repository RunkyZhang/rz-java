package com.rz.langchain.demo.server.agent;

import com.rz.langchain.demo.server.dto.Tuple2;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

import java.util.List;

public interface SimpleParallelAgent {
    @Agent("你是多个ai agent中的组织者。负责调用，循环协调其他ai agent。")
    List<Tuple2<String,  String>> find(@V("mood") String mood);
}
