package com.mmk.llm.constant;

public class ModelType {
    public static final String OPENAI = "openai";
    public static final String ZHIPUAI = "zhipuai";
    public static final String DEFAULT = ZHIPUAI;

    public static String normalize(String modelType) {
        if (modelType == null) {
            return DEFAULT;
        }
        String normalized = modelType.toLowerCase();
        return OPENAI.equals(normalized) ? OPENAI : ZHIPUAI;
    }
}
