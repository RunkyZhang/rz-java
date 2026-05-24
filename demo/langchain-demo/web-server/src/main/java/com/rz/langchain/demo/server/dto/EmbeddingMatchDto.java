package com.rz.langchain.demo.server.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmbeddingMatchDto implements Serializable {
    private Double score;
    private String embeddingId;
    private String text;
    private EmbeddedMetadataDto metadata;
}
