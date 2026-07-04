package com.rz.langchain.demo.server.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface SimpleLoopAgent {
    @Agent("你是多个ai agent中的组织者。负责调用，循环协调其他ai agent。")
    String play(@V("state") String state,
                @V("remainingCards_A") String remainingCardsA,
                @V("remainingCards_B") String remainingCardsB);
}
