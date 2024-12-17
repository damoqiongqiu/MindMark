package com.mmk.llm.service;

import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 * TODO: 切换不同的大模型
 */
public interface ChatModelService {

    String chatModel(String msg);

    Flux<String> chatModelStream(String msg);
}
