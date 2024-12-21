package com.mmk.llm.config;

/**
 * AI 模型常量类
 */
public enum AiModelEnum {
    /**
     * 智谱文生图服务
     */
    ZHI_PU_MODEL_COG_VIEW3_PLUS("cogview-3-plus", "适用于图像生成任务");
    private final String code;
    private final String desc;

    AiModelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "AiModelEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
