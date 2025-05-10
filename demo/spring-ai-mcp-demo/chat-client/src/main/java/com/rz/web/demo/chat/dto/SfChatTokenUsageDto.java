package com.rz.web.demo.chat.dto;

import lombok.Data;

@Data
public class SfChatTokenUsageDto {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
    private SfChatCompletionTokensDetailsDto completion_tokens_details;
}