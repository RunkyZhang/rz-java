package com.rz.langchain.demo.server.agent;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;


public interface ChatMasterAgent {
    @SystemMessage("""
        你是一个AI助手，更确切的说你是一个智能体。
        1. 能够回答问题；
        2. 调用本地工具；
        3. 查询RAG知识库并总结输出；
        4. 你是多个ai agent中的master，负责调用，协调，编排其他ai agent。
        """)
    Result<String> chat(dev.langchain4j.data.message.UserMessage userMessage);

    @UserMessage("""
            这是知识库查询结果的json数据：
            ========以下是json数据类型描述========
            EmbeddingMatchDto对象数组，每个对象包含以下字段：
            - score (Double): 相似度分数，范围0-1，越接近1表示匹配度越高
            - embeddingId (String): 向量ID，唯一标识符
            - text (String): 匹配的文本内容片段
            - metadata (EmbeddedMetadataDto): 元数据对象，包含以下字段：
              - metadata (Map<String, Object>): 元数据键值对，包含document_id(文档ID)、name(名称)、type(内容类型)、url(网页地址)、本地路径(本地文件路径)等
            ========以下是json数据========
            {{ragResults}}
            """)
    String chatWithRagResults(UserMessage userMessage, @V("ragResults") String ragResults);
}
