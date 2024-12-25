package com.mmk.llm.service;

import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @author 大漠穷秋
 * 处理用户交互
 */
public interface ChatClientService {

    String chat(String msg);

    Flux<String> chatStream(String msg);

    String embed(String msg) throws IOException, InterruptedException;

}
