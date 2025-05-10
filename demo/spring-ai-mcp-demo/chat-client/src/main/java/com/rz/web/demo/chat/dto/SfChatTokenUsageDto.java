package com.rz.web.demo.chat.dto;

import lombok.Data;

@Data
public class SfChatTokenUsageDto {
    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private SfChatCompletionTokensDetailsDto completionTokensDetails;
}