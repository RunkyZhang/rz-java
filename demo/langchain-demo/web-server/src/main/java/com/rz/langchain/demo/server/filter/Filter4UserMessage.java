package com.rz.langchain.demo.server.filter;

import dev.langchain4j.store.embedding.filter.Filter;
import lombok.Data;

@Data
public class Filter4UserMessage {
    private String userMessageName;
    private Filter filter;
    private boolean used = false;
}
