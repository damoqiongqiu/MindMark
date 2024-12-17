package com.mmk.llm.service;

import reactor.core.publisher.Flux;

/**
 * @author 大漠穷秋
 * 处理用户交互
 */
public interface ChatClientService {

    String chatClient(String msg);

    Flux<String> chatClientStream(String msg);

}
