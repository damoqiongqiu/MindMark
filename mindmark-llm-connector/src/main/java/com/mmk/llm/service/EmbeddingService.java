package com.mmk.llm.service;

/**
 * @author 大漠穷秋
 * 文本嵌入
 */
public interface EmbeddingService {

    /**
     * 根据嵌入的文本进行查询
     * @param msg
     * @return
     */
    String embed(String msg);

}
