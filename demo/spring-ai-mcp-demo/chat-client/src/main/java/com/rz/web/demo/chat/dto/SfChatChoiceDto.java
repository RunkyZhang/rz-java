package com.rz.web.demo.chat.dto;

import lombok.Data;

@Data
public class SfChatChoiceDto {
    private int index;
    private SfChatOutMessageDto message;
    // stop, eos, length, tool_calls
    private String finish_reason;
    // private List<> tool_calls
}