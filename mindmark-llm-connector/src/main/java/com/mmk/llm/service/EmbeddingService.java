package com.mmk.llm.service;

import org.springframework.ai.embedding.EmbeddingResponse;

/**
 * @author 大漠穷秋
 * 文本嵌入
 */
public interface EmbeddingService {

    EmbeddingResponse embed(String msg);

}
