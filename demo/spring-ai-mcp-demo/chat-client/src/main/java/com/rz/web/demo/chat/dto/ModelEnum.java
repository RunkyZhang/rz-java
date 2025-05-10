package com.rz.web.demo.chat.dto;

public enum ModelEnum {
    // qwen
    QWEN_QWEN3_30B_A3B("Qwen/Qwen3-30B-A3B"),
    QWEN_QWEN3_32B("Qwen/Qwen3-32B"),
    QWEN_QWEN3_14B("Qwen/Qwen3-14B"),
    QWEN_QWEN3_8B("Qwen/Qwen3-8B"),
    QWEN_QWEN3_235B_A22B("Qwen/Qwen3-235B-A22B"),
    QWEN_QWEN2_5_72B_INSTRUCT_128K("Qwen/Qwen2.5-72B-Instruct-128K"),
    QWEN_QWEN2_5_72B_INSTRUCT("Qwen/Qwen2.5-72B-Instruct"),
    QWEN_QWEN2_5_32B_INSTRUCT("Qwen/Qwen2.5-32B-Instruct"),
    QWEN_QWEN2_5_14B_INSTRUCT("Qwen/Qwen2.5-14B-Instruct"),
    QWEN_QWEN2_5_7B_INSTRUCT("Qwen/Qwen2.5-7B-Instruct"),
    QWEN_QWEN2_5_CODER_32B_INSTRUCT("Qwen/Qwen2.5-Coder-32B-Instruct"),
    QWEN_QWEN2_5_CODER_7B_INSTRUCT("Qwen/Qwen2.5-Coder-7B-Instruct"),
    QWEN_QWEN2_7B_INSTRUCT("Qwen/Qwen2-7B-Instruct"),
    QWEN_QWEN2_1_5B_INSTRUCT("Qwen/Qwen2-1.5B-Instruct"),
    QWEN_QWQ_32B_PREVIEW("Qwen/QwQ-32B-Preview"),
    QWEN_QWQ_32B("Qwen/QwQ-32B"),
    PRO_QWEN_QWEN2_5_7B_INSTRUCT("Pro/Qwen/Qwen2.5-7B-Instruct"),
    PRO_QWEN_QWEN2_7B_INSTRUCT("Pro/Qwen/Qwen2-7B-Instruct"),
    PRO_QWEN_QWEN2_1_5B_INSTRUCT("Pro/Qwen/Qwen2-1.5B-Instruct"),
    VENDOR_A_QWEN_QWEN2_5_72B_INSTRUCT("Vendor-A/Qwen/Qwen2.5-72B-Instruct"),

    // deepseek
    DEEPSEEK_AI_DEEPSEEK_R1("deepseek-ai/DeepSeek-R1"),
    DEEPSEEK_AI_DEEPSEEK_V3("deepseek-ai/DeepSeek-V3"),
    DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_32B("deepseek-ai/DeepSeek-R1-Distill-Qwen-32B"),
    DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_14B("deepseek-ai/DeepSeek-R1-Distill-Qwen-14B"),
    DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_7B("deepseek-ai/DeepSeek-R1-Distill-Qwen-7B"),
    DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_1_5B("deepseek-ai/DeepSeek-R1-Distill-Qwen-1.5B"),
    DEEPSEEK_AI_DEEPSEEK_V2_5("deepseek-ai/DeepSeek-V2.5"),
    PRO_DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_7B("Pro/deepseek-ai/DeepSeek-R1-Distill-Qwen-7B"),
    PRO_DEEPSEEK_AI_DEEPSEEK_R1_DISTILL_QWEN_1_5B("Pro/deepseek-ai/DeepSeek-R1-Distill-Qwen-1.5B"),
    PRO_DEEPSEEK_AI_DEEPSEEK_R1("Pro/deepseek-ai/DeepSeek-R1"),
    PRO_DEEPSEEK_AI_DEEPSEEK_V3("Pro/deepseek-ai/DeepSeek-V3"),

    // thudm
    THUDM_GLM_4_9B_CHAT("THUDM/glm-4-9b-chat"),
    THUDM_GLM_Z1_32B_0414("THUDM/GLM-Z1-32B-0414"),
    THUDM_GLM_4_32B_0414("THUDM/GLM-4-32B-0414"),
    THUDM_GLM_Z1_RUMINATION_32B_0414("THUDM/GLM-Z1-Rumination-32B-0414"),
    THUDM_GLM_4_9B_0414("THUDM/GLM-4-9B-0414"),
    PRO_THUDM_CHATGLM3_6B("Pro/THUDM/chatglm3-6b"),
    PRO_THUDM_GLM_4_9B_CHAT("Pro/THUDM/glm-4-9b-chat"),

    // internlm
    INTERNLM_INTERNLM2_5_7B_CHAT("internlm/internlm2_5-7b-chat"),
    INTERNLM_INTERNLM2_5_20B_CHAT("internlm/internlm2_5-20b-chat"),

    // teleai
    TELEAI_TELECHAT2("TeleAI/TeleChat2");

    private final String name;

    ModelEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}