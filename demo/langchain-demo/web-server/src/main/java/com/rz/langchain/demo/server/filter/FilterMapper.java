package com.rz.langchain.demo.server.filter;

import dev.langchain4j.store.embedding.filter.Filter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// 由于langchain4j架构设计缺陷。无法在每次聊天交互时，将特定参数（例如：是否使用知识库，或使用哪个知识库）传给到AiService创建的Assistant的EmbeddingStoreContentRetriever的dynamicFilter中
// 故只能使用FilterMapper作为媒介进行存储传递。
// 如果生产使用需要管控内存占用，清理filter4UserMessages
// 不知道后续langchain4j是否能升级
@Service
public class FilterMapper {
    private final Map<String, Filter4UserMessage> filter4UserMessages = new ConcurrentHashMap<>();

    public Map<String, Filter4UserMessage> getFilter4UserMessages() {
        return filter4UserMessages;
    }

    public void add(String userMessageName, Filter filter) {
        Filter4UserMessage filter4UserMessage = new Filter4UserMessage();
        filter4UserMessage.setUserMessageName(userMessageName);
        filter4UserMessage.setFilter(filter);
        filter4UserMessage.setUsed(false);

        filter4UserMessages.put(userMessageName, filter4UserMessage);
    }

    public Filter4UserMessage get(String userMessageName) {
        return filter4UserMessages.get(userMessageName);
    }

    public void remove(String userMessageName) {
        Filter4UserMessage filter4UserMessage = filter4UserMessages.get(userMessageName);
        if (null == filter4UserMessage || !filter4UserMessage.isUsed()) {
            return;
        }

        filter4UserMessages.remove(userMessageName);
    }
}
