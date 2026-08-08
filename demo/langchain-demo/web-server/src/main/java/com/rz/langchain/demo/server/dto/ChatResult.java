package com.rz.langchain.demo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResult implements Serializable {
    // 是否等待用户回复
    private boolean waitAnswered;
    // 会话ID（waitAnswered=true 时前端需保存，用于 /chatResume）
    private String memoryId;
    // 文本内容：waitAnswered=true 时是问用户的问题；false 时是最终结果
    private String content;
}
