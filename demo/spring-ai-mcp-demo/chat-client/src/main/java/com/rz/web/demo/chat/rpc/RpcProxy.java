package com.rz.web.demo.chat.rpc;

import com.rz.web.demo.chat.JacksonHelper;
import com.rz.web.demo.chat.dto.*;
import feign.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Service
public class RpcProxy {
    @Resource
    private SiliconFlowFeignClient siliconFlowFeignClient;

    public RpcResult<Object> chat(String message) {
        Assert.hasText(message, "Assert.hasText: message");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer sk-hnikuntmbihjuxgdrimxxrfkjzdrfohnaoftxtegcojcupwe");

        SfChatRequestDto body = new SfChatRequestDto();
        body.setModel(ModelEnum.QWEN_QWEN3_235B_A22B.getName()); // 使用枚举值
        body.setStream(false);
        body.setMax_tokens(512);
        body.setEnable_thinking(true);
        body.setThinking_budget(4096);
        body.setMin_p(0.05);
        body.setStop(null);
        body.setTemperature(0.7);
        body.setTop_p(0.7);
        body.setTop_k(50);
        body.setFrequencyPenalty(0.5);
        body.setN(1);
        SfChatResponseFormatDto sfChatResponseFormatDto = new SfChatResponseFormatDto();
        sfChatResponseFormatDto.setType("text");
        body.setResponse_format(sfChatResponseFormatDto);
        // 构造消息列表
        SfChatInMessageDto sfChatInMessageDto = new SfChatInMessageDto();
        sfChatInMessageDto.setRole("user");
        sfChatInMessageDto.setContent(message);
        body.setMessages(Arrays.asList(sfChatInMessageDto));

        RpcResult<Object> result = new RpcResult<>();
        try (Response response = siliconFlowFeignClient.chat(body, httpHeaders)) {
            String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (200 == response.status()) {
                SfChatResponseDto sfChatResponseDto = JacksonHelper.toObj(responseBody, SfChatResponseDto.class, false);
                if (null == sfChatResponseDto) {
                    result.setMessage("failed to parse responseBody to SfChatResponseDto");
                    result.setCode(-1);
                } else {
                    result.setData(sfChatResponseDto);
                    result.setCode(0);
                }
            } else if (400 == response.status() || 429 == response.status() || 503 == response.status()) {
                SfChatResponseDto sfChatResponseDto = JacksonHelper.toObj(responseBody, SfChatResponseDto.class, false);
                if (null == sfChatResponseDto) {
                    result.setMessage("failed to parse responseBody to SfChatResponseDto");
                    result.setCode(-1);
                } else {
                    result.setData(sfChatResponseDto.getData());
                    result.setMessage(sfChatResponseDto.getMessage());
                    result.setCode(0 == sfChatResponseDto.getCode() ? response.status() : sfChatResponseDto.getCode());
                }
            } else {
                result.setMessage(responseBody);
                result.setCode(response.status());
            }
        } catch (Exception e) {
            log.error("failed to invoke siliconFlow's chat api", e);

            result.setCode(-1);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}
