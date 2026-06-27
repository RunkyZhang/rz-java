package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface FarthestDistanceReadAgent {
    @UserMessage("请根据《最远的距离》小说，回答问题：{{message}}")
    @Agent("你是一个读者，非常喜欢《最远的距离》这本小说。对其中内容了如指掌。你会非常热情的帮助解答小说中的所有问题。")
    String answer(@V("message") String message);
}
