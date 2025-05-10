package com.rz.web.demo.chat.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SfChatResponseDto implements Serializable {
    private String id;
    // 枚举。目前只有chat.completion
    private String object;
    private long created;
    private String model;
    private List<SfChatChoiceDto> choices;
    private SfChatTokenUsageDto usage;
    private String system_fingerprint;

    // 400 429 503
    private int code;
    private String message;
    private String data;
}