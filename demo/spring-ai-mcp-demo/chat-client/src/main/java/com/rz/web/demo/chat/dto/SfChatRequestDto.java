package com.rz.web.demo.chat.dto;

import lombok.Data;
import java.util.List;

@Data
public class SfChatRequestDto {
    // 模型名称。
    private String model;
    private List<SfChatInMessageDto> messages;
    // 是否用流方式返回，还是一次性（时间较长）返回。默认true
    private boolean stream = true;
    // 要生成的最大令牌数。 所需范围：1 <= x <= 16384。默认512
    private int max_tokens = 512;
    // 在思考和非思考模式之间切换。默认值为 True。此字段仅适用于 Qwen3。
    private boolean enable_thinking = true;
    // 链输出的最大令牌数。所需范围：1 <= x <= 32768。默认4096。此字段仅适用于 Qwen3。
    private int thinking_budget = 4096;
    // 根据 Token 概率进行调整的动态筛选阈值。所需范围：0 <= x <= 1。默认0.05。此字段仅适用于 Qwen3。
    private double min_p = 0.05;
    // 最多 4 个序列，API 将在其中停止生成更多令牌。返回的文本将不包含停止序列。默认为null。
    private String stop;
    // 确定响应中的随机程度。默认0.7
    private double temperature = 0.7;
    // 参数用于根据累积概率动态调整每个预测标记的选择数。默认0.7
    private double top_p = 0.7;
    // 默认50
    private int top_k = 50;
    // 默认0.5
    private double frequencyPenalty = 0.5;
    // 要返回的代数。默认1
    private int n = 1;
    private SfChatResponseFormatDto response_format;
}