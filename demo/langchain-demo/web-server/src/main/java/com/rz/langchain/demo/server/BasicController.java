/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rz.langchain.demo.server;

import com.rz.langchain.demo.server.dto.ChatMessagesDto;
import com.rz.langchain.demo.server.rpc.RpcProxy;
import com.rz.langchain.demo.server.tools.FormatAddressAgent;
import com.rz.langchain.demo.server.tools.ToolsSelector;
import dev.langchain4j.Experimental;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.*;
import dev.langchain4j.internal.Utils;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Slf4j
@Controller
public class BasicController {
    @Resource
    private RpcProxy rpcProxy;
    @Resource(name = "qwen_3_5_plus")
    private OpenAiChatModel openAiChatModel;
    @Resource
    private StreamingChatModel streamingChatModel;
    @Resource
    private ChatMemory chatMemory;
    @Resource
    private ToolsSelector toolsSelector;

    // http://localhost:8080/hello?value=介绍一下你自己
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "value", defaultValue = "介绍一下你自己") String value) {
        String answer = "";
        answer = getAnswerWithJson(value);
        // answer = getAnswerWithSystemMessage(value);
        // answer = getAnswerWithTools(value);
        // answer = getAnswerWithMemory(value);
        // answer = getAnswerByStream();
        // answer = getAnswerByMessages();
        // answer = getAnswerByImage();
        // answer = getAnswerByImageBase64();
        // answer = getAnswerByMessage(value);
        // answer = getAnswer(value);

        return answer;
    }

    // 测试：给00545579发一条企业微信消息，内容是【厉害呀】
    // 测试：先帮我写一个短文，内容是【父子游崂山】，风格是父子情深，长度是500字”，关键元素是回忆，夕阳，温情。然后把短文内容通过企业微信发给00545579
    // http://localhost:8080/index.html
    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestBody ChatMessagesDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");
        Assert.hasText(requestDto.getMessage(), "Assert.hasText: requestDto.getMessage()");

        // 获取历史会话
        List<ChatMessage> chatMessages = chatMemory.messages();
        chatMessages.addAll(chatMemory.messages());

        // 创建用户消息
        List<Content> contents = new ArrayList<>();
        Content textContent = TextContent.from(requestDto.getMessage());
        contents.add(textContent);
        if (!CollectionUtils.isEmpty(requestDto.getImageBase64s())) {
            for (String imageBase64 : requestDto.getImageBase64s()) {
                ImageContent imageContent = ImageContent.from(imageBase64, "image/jpg");
                contents.add(imageContent);
            }
        }
        ChatMessage userMessage = UserMessage.from(contents);
        chatMessages.add(userMessage);
        // 存储
        chatMemory.add(userMessage);

        int times = 0;
        while (true) {
            // 调用模型
            ChatRequest chatRequest = ChatRequest.builder()
                    .messages(chatMessages)
                    .toolSpecifications(toolsSelector.getToolSpecifications())
                    .build();
            ChatResponse chatResponse = openAiChatModel.chat(chatRequest);
            AiMessage aiMessage = chatResponse.aiMessage();
            chatMessages.add(aiMessage);
            // 存储
            chatMemory.add(aiMessage);

            if (CollectionUtils.isEmpty(aiMessage.toolExecutionRequests())) {
                return aiMessage.text();
            }

            // 限制和LLM交互次数
            times++;
            if (10 < times) {
                return "超过和LLM交互的最大次数。请重试！";
            }

            for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
                Object tools = toolsSelector.getTool(toolExecutionRequest.name());
                if (null == tools) {
                    continue;
                }
                ToolExecutor toolExecutor = new DefaultToolExecutor(tools, toolExecutionRequest);
                String executorResult = toolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
                ToolExecutionResultMessage toolExecutionResultMessages =
                        ToolExecutionResultMessage.from(toolExecutionRequest, executorResult);
                chatMessages.add(toolExecutionResultMessages);
                // 存储
                chatMemory.add(toolExecutionResultMessages);
            }
        }
    }

    private String getAnswer(String value) {
        return openAiChatModel.chat(value);
    }

    private String getAnswerByMessage(String value) {
        ChatMessage userMessage = UserMessage.from(value);
        ChatResponse chatResponse = openAiChatModel.chat(userMessage);
        return chatResponse.aiMessage().text();
    }

    private String getAnswerWithSystemMessage(String value) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = SystemMessage.from("你是AI相关知识学的小助手，叫王二麻子。");
        ChatMessage userMessage = UserMessage.from(value);
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatResponse chatResponse = openAiChatModel.chat(messages);
        return chatResponse.aiMessage().text();
    }

    // 多模态图片识别http body
    //{
    //  "model" : "qwen3.5-plus",
    //  "messages" : [ {
    //    "role" : "user",
    //    "content" : [ {
    //      "type" : "text",
    //      "text" : "看看这个图片内容是什么"
    //    }, {
    //      "type" : "image_url",
    //      "image_url" : {
    //        "url" : "https://c-ssl.duitang.com/uploads/item/202003/21/20200321005919_jfuql.jpg",
    //        "detail" : "low"
    //      }
    //    } ]
    //  } ],
    //  "stream" : false
    //}
    private String getAnswerByImage() {
        UserMessage userMessage = UserMessage.from(
                TextContent.from("看看这个图片内容是什么"),
                ImageContent.from("https://c-ssl.duitang.com/uploads/item/202003/21/20200321005919_jfuql.jpg")
        );
        ChatResponse response = openAiChatModel.chat(userMessage);
        return response.aiMessage().text();
    }

    // 多模态图片识别。使用base64方式传图片
    private String getAnswerByImageBase64() {
        byte[] imageBytes = Utils.readBytes("https://c-ssl.duitang.com/uploads/item/202003/21/20200321005919_jfuql.jpg");
        String base64Data = Base64.getEncoder().encodeToString(imageBytes);
        ImageContent imageContent = ImageContent.from(base64Data, "image/jpg");
        UserMessage userMessage = UserMessage.from(
                TextContent.from("看看这个图片内容是什么"),
                imageContent
        );
        ChatResponse response = openAiChatModel.chat(userMessage);
        return response.aiMessage().text();
    }

    // 多轮对话。给ai更多聊天上下文。后续可以使用聊天记忆功能替代。这只是个demo
    //{
    //  "model" : "qwen3.5-plus",
    //  "messages" : [ {
    //    "role" : "user",
    //    "content" : "你好，我是王二麻子。"
    //  }, {
    //    "role" : "assistant",
    //    "content" : "你好，王二麻子。我是大模型我有什么可以帮助你？"
    //  }, {
    //    "role" : "user",
    //    "content" : "你现在是一个咖啡专家。"
    //  }, {
    //    "role" : "assistant",
    //    "content" : "是的，我是你的咖啡专家。你可以提出咖啡相关问题。"
    //  }, {
    //    "role" : "user",
    //    "content" : "我现在在鞍山我适合喝什么咖啡？"
    //  } ],
    //  "stream" : false
    //}
    private String getAnswerByMessages() {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(UserMessage.from("你好，我是王二麻子。"));
        messages.add(AiMessage.from("你好，王二麻子。我是大模型我有什么可以帮助你？"));
        messages.add(UserMessage.from("你现在是一个咖啡专家。"));
        messages.add(AiMessage.from("是的，我是你的咖啡专家。你可以提出咖啡相关问题。"));
        messages.add(UserMessage.from("我现在在鞍山我适合喝什么咖啡？"));

        ChatResponse response = openAiChatModel.chat(messages);
        return response.aiMessage().text();
    }

    // 聊天流式返回
    // {
    //  "model" : "qwen3.5-plus",
    //  "messages" : [ {
    //    "role" : "user",
    //    "content" : "现在你是一个咖啡专家，请回答我的问题。"
    //  } ],
    //  "stream" : true,
    //  "stream_options" : {
    //    "include_usage" : true
    //  }
    //}
    private String getAnswerByStream() {
        CompletableFuture<ChatResponse> completableFuture = new CompletableFuture<>();
        streamingChatModel.chat(
                List.of(UserMessage.from("现在你是一个咖啡专家，请回答我的问题。")),
                new StreamingChatResponseHandler() {
                    @Override
                    @Experimental
                    public void onPartialThinking(PartialThinking partialThinking) {
                        log.info("partialThinking: {}", partialThinking.text());
                    }

                    @Override
                    public void onPartialResponse(String partialResponse) {
                        log.info("partialResponse: {}", partialResponse);
                    }

                    @Override
                    public void onCompleteResponse(ChatResponse completeResponse) {
                        completableFuture.complete(completeResponse);
                    }

                    @Override
                    public void onError(Throwable error) {
                        log.error("error: {}", error);
                    }
                });

        try {
            return completableFuture.get().aiMessage().text();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getAnswerWithMemory(String message) {
        List<ChatMessage> allChatMessages = chatMemory.messages();
        allChatMessages.addAll(chatMemory.messages());
        ChatMessage userMessage = UserMessage.from(message);
        allChatMessages.add(userMessage);
        AiMessage aiMessage = openAiChatModel.chat(allChatMessages).aiMessage();
        chatMemory.add(userMessage, aiMessage);

        return aiMessage.text();
    }

    private String getAnswerWithTools(String message) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage userMessage = UserMessage.from(message);
        chatMessages.add(userMessage);
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolsSelector.getToolSpecifications())
                .build();
        ChatResponse chatResponse = openAiChatModel.chat(chatRequest);
        AiMessage aiMessage = chatResponse.aiMessage();
        chatMessages.add(aiMessage);
        if (CollectionUtils.isEmpty(aiMessage.toolExecutionRequests())) {
            return aiMessage.text();
        }

        for (ToolExecutionRequest toolExecutionRequest : aiMessage.toolExecutionRequests()) {
            Object tools = toolsSelector.getTool(toolExecutionRequest.name());
            if (null == tools) {
                continue;
            }
            ToolExecutor toolExecutor = new DefaultToolExecutor(tools, toolExecutionRequest);
            String executorResult = toolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
            ToolExecutionResultMessage toolExecutionResultMessages =
                    ToolExecutionResultMessage.from(toolExecutionRequest, executorResult);

            chatMessages.add(toolExecutionResultMessages);
        }

        chatResponse = openAiChatModel.chat(chatMessages);
        return chatResponse.aiMessage().text();
    }

    // 测试prompt：解析以下地址：快递请邮寄到上海市浦东新区陆家嘴环路1000号恒生银行大厦18层，13688889999王五先生收就好了。电话记得要脱敏，如果人没接电话那么就把快递请放前台
    private String getAnswerWithJson(String message) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage userMessage = UserMessage.from(message);
        ChatMessage systemMessage = UserMessage.from(FormatAddressAgent.SYSTEM_PROMPT);
        chatMessages.add(systemMessage);
        chatMessages.add(userMessage);
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(chatMessages)
                .build();

        ChatResponse chatResponse = openAiChatModel.chat(chatRequest);
        return chatResponse.aiMessage().text();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }
}
