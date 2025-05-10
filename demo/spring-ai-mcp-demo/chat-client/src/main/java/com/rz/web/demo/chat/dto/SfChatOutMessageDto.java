package com.rz.web.demo.chat.dto;

import lombok.Data;

@Data
public class SfChatOutMessageDto {
    private String role;
    private String content;
    private String reasoning_content;
}